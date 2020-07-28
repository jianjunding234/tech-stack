package com.dingjianjun.basetech.nio;

import com.google.common.base.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * @author : Jianjun.Ding
 * @description: Server使用Netty
 * @date 2020/5/26
 */
@Slf4j
public class NettyServer {
    static BizThreadGroup bizThreads = new BizThreadGroup(4);
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        FlowControlChannelHandler flowCtlHandler = new FlowControlChannelHandler(bizThreads);
      //  MyInBoundHandler myInBoundHandler = new MyInBoundHandler();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            // 流量控制保护
                            pipeline.addLast("FlowControlChannelHandler", flowCtlHandler);
//                            pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
                            // 单个链路的流量整形
                            pipeline.addLast(new ChannelTrafficShapingHandler(1024*1024, 1024*1024, 1000));
                          //  pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new MyInBoundHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = serverBootstrap.bind(8888).sync(); // 阻塞当前线程等待服务启动
            f.channel().closeFuture().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            bossGroup.shutdownGracefully();
                            workerGroup.shutdownGracefully();
                            log.info("{} 链路关闭", f.channel().toString());

                        }
                    })
                    .sync(); // 阻塞当前线程等待服务停止
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup, 释放所有资源
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }


    }

   // @ChannelHandler.Sharable
    static class MyInBoundHandler extends ChannelInboundHandlerAdapter {
        private volatile boolean authPassed = false;
        private static final ByteBuf HEARTBEAT_PING =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("ping", Charsets.UTF_8));
        private static final ByteBuf HEARTBEAT_PONG =
               Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("pong", Charsets.UTF_8));
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channel注册 channelId:" + ctx.channel().id());
            ctx.fireChannelRegistered();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            String channelId = ctx.channel().id().asLongText();
            System.out.println("开始读 channelRead");
            if (msg instanceof ByteBuf) {
                ByteBuf buf = (ByteBuf) msg;
//                int size = buf.writerIndex();
                byte[] data = new byte[buf.readableBytes()];
                buf.getBytes(0, data);
                String str = new String(data);
                ReferenceCountUtil.release(msg);
                if (!authPassed) {
                    // 鉴权
                    if (!StringUtils.isBlank(str) && "token_123".equals(str)) {
                        authPassed = true;
                    } else {
                        authPassed = false;
                        ctx.channel().close();
                    }
                    return;
                }

                // 业务逻辑封装成task，提交给业务线程池
                Runnable task = () -> {
                    System.out.println("channelId:" + channelId + " read data:" + str);
                    System.out.println("start processing business...");

                };
                bizThreads.execute(channelId, task);
              // ctx.write(HEARTBEAT_PONG);
            }

           // ctx.write(HEARTBEAT_PONG);

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelReadComplete channelId:" + ctx.channel().id());
//            super.channelReadComplete(ctx);
            ctx.flush();
        //    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端断开连接--通道未注册 channelUnregistered");
            ctx.fireChannelUnregistered();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            String channelId = ctx.channel().id().asLongText();
            System.out.println("客户端建立连接后--激活通道  channelId:" + channelId);

            bizThreads.register(channelId);
            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            String channelId = ctx.channel().id().asLongText();
            System.out.println("客户端断开连接--关闭通道  channelId:" + channelId);
            bizThreads.unregister(channelId);
            ctx.fireChannelInactive();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            System.out.println("channelId:" + ctx.channel().id() + " 出现异常");
            cause.printStackTrace();
            ctx.close();
        }

       /**
        * 处理事件
        * @param ctx
        * @param evt 处理事件
        * @throws Exception
        */
       @Override
       public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//           if (evt instanceof IdleStateEvent) {
//               System.out.println("触发IdleStateEvent事件，状态：" + ((IdleStateEvent) evt).state());
//               if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
//                   System.out.println("发送消息：ping");
//                   ctx.writeAndFlush(HEARTBEAT_PING.duplicate())
//                           .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
//               } else if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {
//
//               }
//           } else {
//               super.userEventTriggered(ctx, evt);
//           }
       }
   }

}
