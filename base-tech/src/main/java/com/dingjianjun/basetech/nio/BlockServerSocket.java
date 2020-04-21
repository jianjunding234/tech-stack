package com.dingjianjun.basetech.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author : Jianjun.Ding
 * @description: BIO server socket
 * @date 2020/4/8
 */
public class BlockServerSocket {
    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             FileChannel outChannel = FileChannel.open(
                     Paths.get("/data/1.wav"),
                     StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(8888));
            //接受链接
            SocketChannel socketChannel = serverSocketChannel.accept();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            //阻塞
            while (socketChannel.read(buf) != -1) {
                buf.flip();
                outChannel.write(buf);
                buf.clear();
            }

            buf.put("accept success".getBytes("UTF-8"));
            buf.flip();
            socketChannel.write(buf);
            buf.clear();
            socketChannel.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
