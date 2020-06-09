package com.dingjianjun.basetech.nio;

import com.google.common.base.Charsets;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/**
 * @author : Jianjun.Ding
 * @description: NIO Socket Client
 * @date 2020/4/8
 */
public class NoBlockingSocketClient {
    public static void main(String[] args) {
        SocketChannel socketChannel = null;
       // FileChannel fileChannel = null;

        try {
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
            // 设置非阻塞模式
            socketChannel.configureBlocking(false);
//            fileChannel = FileChannel.open(
//                    Paths.get("/data/1.wav"), StandardOpenOption.READ);
            ByteBuffer buf = ByteBuffer.allocate(1024);

//            while(fileChannel.read(buf) != -1) {
//                buf.flip();
//                socketChannel.write(buf);
//                buf.clear();
//            }

            while (true) {
                buf.put("from client: Hi~server".getBytes(Charsets.UTF_8));
                buf.flip();
                socketChannel.write(buf);
                buf.clear();
                try {
                    TimeUnit.SECONDS.sleep(2L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                socketChannel.read(buf);
                buf.flip();
                System.out.println(new String(buf.array()));
                buf.clear();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (null != socketChannel) {
                try {
                    socketChannel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

//            if (null != fileChannel) {
//                try {
//                    fileChannel.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//
//            }
        }



    }
}
