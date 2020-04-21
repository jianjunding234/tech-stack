package com.dingjianjun.basetech.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author : Jianjun.Ding
 * @description: BIO socket client
 * @date 2020/4/8
 */
public class BlockSocketClient {
    public static void main(String[] args) {
        SocketChannel socketChannel = null;
        FileChannel fileChannel = null;
        try {
            //获取通道
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
            //文件通道
            fileChannel = FileChannel.open(
                    Paths.get("/data/1.wav"), StandardOpenOption.READ);
            //缓冲区
            ByteBuffer buf = ByteBuffer.allocate(1024);
            while(fileChannel.read(buf) != -1) {
                //切换模式
                buf.flip();
                //发送字节流
                socketChannel.write(buf);
                //清空缓冲区
                buf.clear();
            }
            //关闭通道输出
            socketChannel.shutdownOutput();

            int len;
            while((len = socketChannel.read(buf)) != -1) {
                buf.flip();
                System.out.println(new String(buf.array(), 0, len));
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

            if (null != fileChannel) {
                try {
                    fileChannel.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }

    }
}
