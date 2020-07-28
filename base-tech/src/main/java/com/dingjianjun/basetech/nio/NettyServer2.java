package com.dingjianjun.basetech.nio;

import com.google.common.base.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


/**
 * @author : Jianjun.Ding
 * @description: Server使用Netty
 * @date 2020/5/26
 */
@Slf4j
public class NettyServer2 {
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
                            pipeline.addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
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


    static class MyInBoundHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            log.info("msg:{}", msg);
        }
    }

}
