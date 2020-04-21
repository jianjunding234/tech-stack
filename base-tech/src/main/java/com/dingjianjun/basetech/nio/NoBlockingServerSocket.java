package com.dingjianjun.basetech.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

/**
 * @author : Jianjun.Ding
 * @description: NIO Server Socket
 * @date 2020/4/8
 */
public class NoBlockingServerSocket {

    public static void main(String[] args) {
        ServerSocketChannel ssc = null;
        try {
            //获取通道
            ssc = ServerSocketChannel.open();
            //切换成非阻塞模式
            ssc.configureBlocking(false);
            //绑定连接
            ssc.bind(new InetSocketAddress(8888));
            //获取选择器
            Selector selector = Selector.open();
            //通道注册到选择器上，指定接收 "监听通道"事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            //轮询获取选择器已就绪事件，如果没有已就绪事件，则线程阻塞
            while (selector.select() > 0) {
                //获取选择器所有注册的选择键（已就绪的监听事件）
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()) {
                    //获取已就绪的事件（不同的事件做不同的事情）
                    SelectionKey selectionKey = iterator.next();
                    //接收就绪（准备接受新的连接）
                    if (selectionKey.isAcceptable()) {
                        //接受新的连接，拿到客户端的连接
                        SocketChannel socketChannel = ssc.accept();
                        //切换成非阻塞模式
                        socketChannel.configureBlocking(false);
                        //客户端通道注册到选择器上，监听读就绪事件
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) { //读就绪
                        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                        long curTime = System.currentTimeMillis();
                        FileChannel outChannel = FileChannel.open(
                                Paths.get("/data/" + curTime + ".wav"),
                                StandardOpenOption.WRITE, StandardOpenOption.CREATE);
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        while (socketChannel.read(buf) != -1) {
                            buf.flip();
                            outChannel.write(buf);
                            buf.clear();
                        }

                        outChannel.close();
                    }

                    //移除已处理的选择键
                    iterator.remove();
                }

            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (ssc != null) {
                try {
                    ssc.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


}
