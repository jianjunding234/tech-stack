package com.dingjianjun.basetech.nio;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Jianjun.Ding
 * @description: NIO 多线程处理 ServerSocket
 * @date 2020/5/26
 */
public class NioThreadsSocket {
    private ServerSocketChannel ssc = null;
    private Selector selector1 = null;
    private Selector selector2 = null;
    private Selector selector3 = null;
    private int port = 8888;

    public void initServer() {
        try {
            // 获取通道
            ssc = ServerSocketChannel.open();
            // 设置成非阻塞模式
            ssc.configureBlocking(false);
            // 绑定端口
            ssc.bind(new InetSocketAddress(port));
            selector1 = Selector.open();
            selector2 = Selector.open();
            selector3 = Selector.open();
            ssc.register(selector1, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        NioThreadsSocket nts = new NioThreadsSocket();
        nts.initServer();
        NioThread t1 = new NioThread(nts.selector1, 2);
        NioThread t2 = new NioThread(nts.selector2);
        NioThread t3 = new NioThread(nts.selector3);
        t1.start();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();
        t3.start();

        System.out.println("服务器已启动......");

    }



    private static class NioThread extends Thread {
        private Selector selector = null;
        private static int selectors = 0;
        private int id = 0;

        private static AtomicInteger idx = new AtomicInteger();
        private static BlockingQueue<SocketChannel>[] queues;

        public NioThread(Selector selector, int n) {
            this.selector = selector;
            selectors = n;
            queues = new BlockingQueue[selectors];
            for (int i = 0; i < queues.length; i++) {
                queues[i] = new LinkedBlockingQueue<>(100);
            }

            System.out.println("Boss线程已启动......");
        }

        public NioThread(Selector selector) {
            this.selector = selector;
            id = idx.getAndIncrement() % selectors;
            System.out.println("id-" + id + "worker 线程已启动......");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 等待通道变成就绪，设置等待超时时间
                    while (selector.select(10) > 0) {
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

                    // 队列非空
                    if (!queues[id].isEmpty()) {
                        SocketChannel client = queues[id].take();
                        ByteBuffer buf = ByteBuffer.allocateDirect(8192);
                        client.register(selector, SelectionKey.OP_READ, buf);
                        System.out.println("-------------------------------------------");
                        System.out.println("新的客户端port:" + client.socket().getPort() + " 分配到：" + id);
                        System.out.println("-------------------------------------------");

                    }

                    TimeUnit.SECONDS.sleep(1);
                }

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }

        }

        private void acceptHandler(SelectionKey key) {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
            try {
                // 接受新的socket连接，拿到客户端的连接
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                int index = idx.getAndIncrement() % selectors;
                queues[index].add(socketChannel);
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

    }







}
