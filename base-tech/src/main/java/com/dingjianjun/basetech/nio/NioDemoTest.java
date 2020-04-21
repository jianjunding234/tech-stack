package com.dingjianjun.basetech.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * @author : Jianjun.Ding
 * @description: ByteBuffer
 * @date 2020/4/8
 */
public class NioDemoTest {
    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        System.out.println("初始capacity:" + buffer.capacity() + " 初始position:" + buffer.position()
                + " 初始limit:" + buffer.limit());
        String data = "Hi";
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
            FileChannel outChannel = write.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
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

}
