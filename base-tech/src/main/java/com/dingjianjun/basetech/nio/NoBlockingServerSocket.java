package com.dingjianjun.basetech.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description: NIO Server Socket
 * @date 2020/4/8
 */
public class NoBlockingServerSocket {
    private ServerSocketChannel ssc = null;
    private Selector selector = null;
    private int port = 8888;

    public void initServer() {
        try {
            // 获取通道
            ssc = ServerSocketChannel.open();
            // 设置成非阻塞模式
            ssc.configureBlocking(false);
            // 绑定端口
            ssc.bind(new InetSocketAddress(port));
            // 获取选择器
            selector = Selector.open();  // select poll epoll
            // 通道注册到选择器上，指定"监听通道" 的事件为接受socket 连接
            ssc.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        initServer();
        System.out.println("服务器已经启动......");
        try {
            while (true) {
                // 等待通道变成就绪，设置等待超时时间
                while (selector.select() > 0) {
                    // 获取选择器所有注册的选择键（已就绪的通道）
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        //移除已处理的选择键
                        it.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        }
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        try {
            // 接受新的socket连接，拿到客户端的连接
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            System.out.println("-------------------------------------------");
            System.out.println("新客户端：" + socketChannel.getRemoteAddress());
            System.out.println("-------------------------------------------");
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8192);
            // 客户端通道注册到选择器上，监听读就绪事件, 缓冲区对象附在SelectionKey上
            socketChannel.register(selector, SelectionKey.OP_READ, byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHandler(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buf = (ByteBuffer) key.attachment();
        buf.clear();
        try {
            int len;
            while (true) {
                len = channel.read(buf);
                if (len > 0) {
                    buf.flip();
                    while (buf.hasRemaining()) {
                        channel.write(buf);
                    }
                    buf.clear();
                } else if (len == 0) {
                    break;
                } else {
                    channel.close();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NoBlockingServerSocket().start();
    }
}
