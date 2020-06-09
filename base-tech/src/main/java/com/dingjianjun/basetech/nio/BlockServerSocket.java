package com.dingjianjun.basetech.nio;

import com.dingjianjun.basetech.concurrent.MyThreadFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description: BIO server socket
 * @date 2020/4/8
 */
public class BlockServerSocket {
    // 未处理连接的最大数量
    private static final int BACK_LOG = 2;

    //server socket listen property:
    private static final int RECEIVE_BUFFER = 10;
    private static final int SO_TIMEOUT = 0;
    private static final boolean REUSE_ADDR = false;
    //client socket listen property on server endpoint:
    private static final boolean CLI_KEEPALIVE = false;
    private static final boolean CLI_OOB = false;
    private static final int CLI_REC_BUF = 20;
    private static final boolean CLI_REUSE_ADDR = false;
    private static final int CLI_SEND_BUF = 20;
    private static final boolean CLI_LINGER = true;
    private static final int CLI_LINGER_N = 0;
    private static final int CLI_TIMEOUT = 0;
    private static final boolean CLI_NO_DELAY = false;

    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             FileChannel outChannel = FileChannel.open(
                     Paths.get("E:/testData/001.wav"),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            // 绑定端口 监听
            serverSocketChannel.bind(new InetSocketAddress(8888));
            ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4,
                    0L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(4),
                    new MyThreadFactory("nio-test"),
                    new ThreadPoolExecutor.AbortPolicy());

            while (true) {
                // 接受连接 阻塞
                SocketChannel socketChannel = serverSocketChannel.accept();
                executor.submit(() -> {
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    try {
                        while (true) {
                            // 阻塞
                            int len;
                            while ((len = socketChannel.read(buf)) != -1) {
                                buf.flip();
                                // outChannel.write(buf);
                                System.out.println(new String(buf.array(), 0, len));
                                buf.clear();
                            }

                            buf.put("From xx:Hello client~".getBytes("UTF-8"));
                            buf.flip();
                            socketChannel.write(buf);
                            buf.clear();
                            TimeUnit.SECONDS.sleep(5);
                        }
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    } finally {
                        try {
                            socketChannel.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
