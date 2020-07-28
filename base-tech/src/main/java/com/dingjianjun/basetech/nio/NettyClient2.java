package com.dingjianjun.basetech.nio;

import com.google.common.base.Charsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Jianjun.Ding
 * @description: netty 客户端
 * @date 2020/5/26
 */
@Slf4j
public class NettyClient2 {
    public static void main(String[] args) {
        EventLoopGroup work = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(work).channel(NioSocketChannel.class)
                    .remoteAddress("127.0.0.1", 8888)
                  //  .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            System.out.println("初始化client");
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    System.out.println("异步回调>>>连接已建立");
                    if (future.isSuccess()) {
//                        ByteBufAllocator alloc = future.channel().alloc();
//                        ByteBuf buffer = alloc.buffer();
//                        System.out.println("refCnt:" + buffer.refCnt());
//                        assert buffer.refCnt() == 1;
//
//                        buffer.release();
//                        System.out.println("refCnt:" + buffer.refCnt());
//                        assert buffer.refCnt() == 1;
//
//
//
//                        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello".getBytes(Charsets.UTF_8));
//                        future.channel().writeAndFlush(byteBuf).sync();
                        ByteBuf buf =
                               Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hello\n", Charsets.UTF_8));

                        for (int i = 0; i < 10; i++) {
                            future.channel().writeAndFlush(buf).sync();

                        }

                    } else {
                        future.channel().close();
                    }
                }
            });
            Channel channel = channelFuture.channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池释放所有资源
            work.shutdownGracefully();
        }


    }

    private static class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
        private static final ByteBuf BUF =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hello", Charsets.UTF_8));

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            log.info("ClientHandler channelRegistered");
            super.channelRegistered(ctx);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
//            int size = msg.writerIndex();
//            byte[] data = new byte[size];
//            msg.getBytes(0, data);
//            String str = new String(data);
//            String[] split = str.split("\n");
//            for (String item : split) {
//                System.out.print("server response：" + item + " ");
//            }
          //  ctx.write(BUF);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.info("ClientHandler channelActive");
            ctx.fireChannelActive();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

}
