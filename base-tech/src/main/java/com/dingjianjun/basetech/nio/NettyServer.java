package com.dingjianjun.basetech.nio;

import com.google.common.base.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


/**
 * @author : Jianjun.Ding
 * @description: Server使用Netty
 * @date 2020/5/26
 */
public class NettyServer {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(2);
        EventLoopGroup work = new NioEventLoopGroup(3);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
      //  MyInBoundHandler myInBoundHandler = new MyInBoundHandler();
        try {
            serverBootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            ChannelPipeline pipeline = nioSocketChannel.pipeline();
                            pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new MyInBoundHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind(8888)
                    .sync() // 阻塞当前线程等待服务启动
                    .channel()
                    .closeFuture()
                    .sync(); // 阻塞当前线程等待服务停止
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup, 释放所有资源
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }


    }

   // @ChannelHandler.Sharable
    static class MyInBoundHandler extends ChannelInboundHandlerAdapter {
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
            System.out.println("开始读 channelRead");
            if (msg instanceof ByteBuf) {
                ByteBuf buf = (ByteBuf) msg;
                int size = buf.writerIndex();
                byte[] data = new byte[size];
                buf.getBytes(0, data);
                String str = new String(data);
                String[] split = str.split("\n");
                for (String item : split) {
                    System.out.print("收到消息：" + item + " ");
                }

              // ctx.write(HEARTBEAT_PONG);
            }

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            System.out.println("channelReadComplete channelId:" + ctx.channel().id());
//            super.channelReadComplete(ctx);
            ctx.flush();
           // ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端断开连接--通道未注册 channelUnregistered");
            ctx.fireChannelUnregistered();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端建立连接后--激活通道  channelId:" + ctx.channel().id());
            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端断开连接--关闭通道  channelInactive");
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
           if (evt instanceof IdleStateEvent) {
               System.out.println("触发IdleStateEvent事件，状态：" + ((IdleStateEvent) evt).state());
               if (((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
                   System.out.println("发送消息：ping");
                   ctx.writeAndFlush(HEARTBEAT_PING.duplicate())
                           .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
               } else if (((IdleStateEvent) evt).state() == IdleState.WRITER_IDLE) {

               }
           } else {
               super.userEventTriggered(ctx, evt);
           }
       }
   }

}
