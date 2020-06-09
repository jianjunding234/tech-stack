package com.dingjianjun.basetech.nio;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : Jianjun.Ding
 * @description: 模拟Netty的线程模型
 * @date 2020/5/26
 */
public class NioThreadsSocketV2 {
    public static void main(String[] args) {
        EventLoopGroup boss = new EventLoopGroup(1);
        EventLoopGroup work = new EventLoopGroup(3);
        new ServerBootStrap().group(boss, work).bind(8888);
    }

    static class ServerBootStrap {
        private EventLoopGroup boss;
        private EventLoopGroup work;
        public ServerBootStrap group(EventLoopGroup boss, EventLoopGroup work) {
            this.boss = boss;
            this.work = work;
            return this;
        }

        public void bind(int port) {
            try {
                // 打开 ServerSocket 通道
                ServerSocketChannel ssc = ServerSocketChannel.open();
                // 设置非阻塞
                ssc.configureBlocking(false);
                // 绑定端口
                ssc.bind(new InetSocketAddress(port));
                // boss 选择一个EventLoop
                EventLoop eventLoop = boss.choose();
                System.out.println("server bind 端口：" + port);
                // ServerSocketChannel异步注册到Selector
                eventLoop.execute(() -> {
                    ServerAcceptor handler = new ServerAcceptor(ssc, work);
                    try {
                        System.out.println("Thread name:" + eventLoop.name +  " 负责ServerSocketChannel注册到Selector");
                        ssc.register(eventLoop.selector, SelectionKey.OP_ACCEPT, handler);
                    } catch (ClosedChannelException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static class EventLoopGroup {
        private final AtomicInteger cnt = new AtomicInteger(0);
        private int nThreads;
        private EventLoop[] eventLoops;

        EventLoopGroup(int nThreads) {
            this.nThreads = nThreads;
            eventLoops = new EventLoop[nThreads];
            for (int i = 0; i < nThreads; i++) {
                eventLoops[i] = new EventLoop("T" + i);
            }
        }

        public EventLoop choose() {
            int index = cnt.getAndIncrement() % eventLoops.length;
            return eventLoops[index];
        }
    }

    /**
     * 执行器
     */
    static class EventLoop implements Executor {
        private Selector selector;
        private Thread thread;
        private String name;
        private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(50);

        public EventLoop(String name) {
            this.name = name;
            try {
                this.selector = Selector.open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void execute(Runnable task) {
            try {
                // task 进入消息队列
                queue.put(task);
                // wakeup 唤醒因调用Selector.select()阻塞的线程，若当前Selector没有select操作，下次调用select操作立即返回
                this.selector.wakeup();
                if (!inEventLoop()) {
                    new Thread(() -> {
                        thread = Thread.currentThread();
                        try {
                            EventLoop.this.run();
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        // loop 死循环，在线程中执行，线程在execute方法中创建
        private void run() throws IOException, InterruptedException {
            for (;;) {
                // 若之前调用过一次selector.wakeup(), 当前select() 立即返回，否则阻塞等待有通道就绪
                int keysNum = selector.select();
                if (keysNum > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> it = selectionKeys.iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        it.remove();
                        Handler handler = (Handler) key.attachment();
                        handler.handle();
                    }
                }

                // 消费队列中的消息
                runForTask();
            }
        }

        private void runForTask() throws InterruptedException {
            for (int i = 0; i < 5; i++) {
                // 消费队列中的消息，超时等待
                Runnable task = queue.poll(10, TimeUnit.MILLISECONDS);
                if (null != task) {
                    task.run();
                }
            }
        }

        private boolean inEventLoop() {
            return thread == Thread.currentThread();
        }
    }

    interface Handler {
        void handle();

    }

    static class ServerAcceptor implements Handler {
        private ServerSocketChannel ssc;
        private EventLoopGroup cGroup;
        ServerAcceptor(ServerSocketChannel ssc, EventLoopGroup cGroup) {
            this.ssc = ssc;
            this.cGroup = cGroup;
        }

        @Override
        public void handle() {
            try {
                SocketChannel client = ssc.accept();
                // 设置非阻塞模式
                client.configureBlocking(false);
                // 设置TCP
                client.setOption(StandardSocketOptions.TCP_NODELAY, true);
                EventLoop eventLoop = cGroup.choose();
                // SocketChannel 异步注册到Selector
                eventLoop.execute(() -> {
                    ClientRead clientRead = new ClientRead(client);
                    try {
                        String name = Thread.currentThread().getName() + "-" + eventLoop.name;
                        System.out.println("新的客户端 sended to " + name);
                        client.register(eventLoop.selector, SelectionKey.OP_READ, clientRead);
                    } catch (ClosedChannelException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class ClientRead implements Handler {
        private SocketChannel channel;
        ClientRead(SocketChannel channel) {
            this.channel = channel;
        }

        @Override
        public void handle() {
            ByteBuffer buf = ByteBuffer.allocateDirect(1024);
            try {
                while (channel.read(buf) > 0) {
                    buf.flip();
                    byte[] data = new byte[buf.limit()];
                    buf.get(data);
                    buf.clear();
                    System.out.println(new String(data));
                }
                for (int i = 0; i < 10; i++) {
                    buf.put("a".getBytes(Charsets.UTF_8));
                }
                buf.flip();
                channel.write(buf);
                buf.clear();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
