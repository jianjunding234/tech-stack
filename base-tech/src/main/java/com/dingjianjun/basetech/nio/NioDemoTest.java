package com.dingjianjun.basetech.nio;

import com.google.common.base.Charsets;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


/**
 * @author : Jianjun.Ding
 * @description: ByteBuffer
 * @date 2020/4/8
 */
public class NioDemoTest {
    public static void main(String[] args) throws Exception {
        testMappedByteBuffer("E:/testData/20200525.txt");

    }

    public static void testByteBuffer() throws UnsupportedEncodingException {
        // 堆上分配
//        ByteBuffer buffer = ByteBuffer.allocate(1024);

        /**
         * HeapByteBuffer 与 DirectByteBuffer的区别：HeapByteBuffer 是java堆上分配，DirectByteBuffer 是jvm进程的java堆外申请的内存，使用了 malloc 申请的内存，其实是C heap
         * DirectByteBuffer 比 HeapByteBuffer  少一次内存拷贝
         *
         * 为什么在执行网络IO或者文件IO时，一定要通过堆外内存呢
         * 把一个地址通过JNI传递给底层的C库的时候，有一个基本的要求，就是这个地址上的内容不能失效
         */
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        System.out.println("初始capacity:" + buffer.capacity() + " 初始position:" + buffer.position()
                + " 初始limit:" + buffer.limit());
        String data = "Hi,sx";
        buffer.put(data.getBytes("UTF-8"));

        System.out.println("操作后capacity:" + buffer.capacity() + " position:" + buffer.position()
                + " limit:" + buffer.limit());

        buffer.flip();
        System.out.println("flip 操作后 capacity:" + buffer.capacity() + " 初始position:" + buffer.position()
                + " 初始limit:" + buffer.limit());

        int limit = buffer.limit();
        int position = buffer.position();
        byte[] dest = new byte[limit - position];
        buffer.get(dest);
        System.out.println("content:" + new String(dest, "UTF-8"));
        // 空间整理
        buffer.compact();
        System.out.println("compact 操作后 capacity:" + buffer.capacity() + " 初始position:" + buffer.position()
                + " 初始limit:" + buffer.limit());
    }


    public static void transferFileWithIo(File src, File target) {
        long beginTime = System.currentTimeMillis();
        if (!target.exists()) {
            try {
                target.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(src));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target))) {
            byte[] buffer = new byte[1024 * 1024];
            int len;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }

            bos.flush();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("IO spend " + (endTime - beginTime) + "ms");

    }

    public static void transferFileWithNio(File src, File target) {
        long beginTime = System.currentTimeMillis();

        try {
            RandomAccessFile read = new RandomAccessFile(src, "rw");
            RandomAccessFile write = new RandomAccessFile(target, "rw");
            FileChannel inChannel = read.getChannel();
            // 零拷贝
            // inChannel.transferFrom()
           // inChannel.transferTo()
            FileChannel outChannel = write.getChannel();
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                outChannel.write(buffer);
                buffer.clear();
            }


            inChannel.close();
            outChannel.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("NIO spend " + (endTime - beginTime) + "ms");
    }

    public static void testMappedByteBuffer(String path) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            raf.write("Hello,FXZ\n".getBytes(Charsets.UTF_8));
            raf.write("Hello,SX\n".getBytes(Charsets.UTF_8));
            System.out.println("write------");
            raf.seek(4);
            raf.write("test111\n".getBytes(Charsets.UTF_8));
            System.out.println("seek------");
            FileChannel fileChannel = raf.getChannel();
            // Linux系统调用mmap(), 用户buffer与内核buffer的内存地址映射   堆外 与文件映射的内存区域
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 4096);
            // 不是系统调用  但是数据会到达 内核的pagecache
            map.put("aaa".getBytes(Charsets.UTF_8));
            System.out.println("put------");
            RandomAccessFile raf2 = new RandomAccessFile("E:/testData/20200526.txt", "rw");
            FileChannel targetChannel = raf2.getChannel();
            // Linux系统调用sendFile()实现零拷贝，数据在内核buffer中copy，不经过用户buffer
            fileChannel.transferTo(0, 4096, targetChannel);
           // fileChannel.transferFrom(targetChannel, 0, 406);

            raf.seek(0);
            ByteBuffer buf = ByteBuffer.allocateDirect(8192);
            fileChannel.read(buf);

            System.out.println(buf);
            buf.flip();
            System.out.println(buf);

            for (int i = 0; i < buf.limit(); i++) {
                System.out.print(((char)buf.get(i)));
            }

            fileChannel.close();
            raf.close();
            map.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
