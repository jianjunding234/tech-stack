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
                    Paths.get("E:/testData/small_0.wav"), StandardOpenOption.READ);
            //缓冲区 堆上分配
            ByteBuffer buf = ByteBuffer.allocate(1024);
            while (true) {
                buf.put("From fxz: Hello~Server".getBytes(Charsets.UTF_8));
                //切换模式
                buf.flip();
                socketChannel.write(buf);
                buf.clear();
//            while(fileChannel.read(buf) != -1) {
//                //切换模式
//                buf.flip();
//                //发送字节流
//                socketChannel.write(buf);
//                //清空缓冲区
//                buf.clear();
//            }
                //关闭通道输出
                socketChannel.shutdownOutput();

                int len;
                while((len = socketChannel.read(buf)) != -1) {
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, len));
                    buf.clear();
                }

                TimeUnit.SECONDS.sleep(5);
            }
        } catch (IOException | InterruptedException ex) {
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
