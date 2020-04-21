package com.dingjianjun.basetech.util;

import java.io.*;

/**
 * @author : Jianjun.Ding
 * @description: pcm音频转换工具类
 * @date 2020/4/8
 */
public class PcmConvertUtils {
    /**
     * pcm转wav
     * @param pcmPath pcm 源文件路径
     * @param destWavPath 生成16Khz 16bit wav文件路径
     */
    public static void processPcm(String pcmPath, String destWavPath) {
        File pcmFile = new File(pcmPath);
        File wavFile = new File(destWavPath);

        try (FileInputStream fis = new FileInputStream(pcmFile);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             FileOutputStream fos = new FileOutputStream(wavFile)) {
            WaveHeader waveHeader = generateWav16k16BitHeader((int)pcmFile.length());
            baos.write(waveHeader.getHeader());

            byte[] buf = new byte[16000];
            int count;
            while((count = fis.read(buf, 0, buf.length)) > 0) {
                baos.write(buf, 0, count);
            }

            baos.flush();
            fos.write(baos.toByteArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 生成16K16BIT的文件头
     *
     * @param length
     * @return
     */
    public static WaveHeader generateWav16k16BitHeader(int length) {
        WaveHeader header = new WaveHeader();
        header.chunkSize = length + (44 - 8); // 初始化大小
        header.subChunk1Size = 16;
        header.bitsPerSample = 16;
        header.numChannels = 1;
        header.audioFormat = 0x0001;
        header.sampleRate = 16000;
        header.blockAlign = (short) (header.numChannels * header.bitsPerSample / 8);
        header.byteRate = header.sampleRate * header.blockAlign;
        header.subChunk2Size = length; // 初始化数据大小
        return header;
    }


    public static class WaveHeader {
        /**
         * ASCII 码表示的“RIFF”。（0x52494646）
         */
        public final char[] chunkID = {'R', 'I', 'F', 'F'};
        /**
         * 36+SubChunk2Size，或是
         * 4 + ( 8 + SubChunk1Size ) + ( 8 + SubChunk2Size )，
         * 这是整个数据块的大小（不包括ChunkID和ChunkSize的大小）
         */
        public int chunkSize;
        /**
         * ASCII 码表示的“WAVE”。（0x57415645）
         */
        public char[] format = {'W', 'A', 'V', 'E'};
        /**
         * 新的数据块（格式信息说明块）
         * ASCII 码表示的“fmt ”——最后是一个空格。（0x666d7420）
         */
        public char[] subChunk1ID = {'f', 'm', 't', ' '};
        /**
         * 本块数据的大小（对于PCM，值为16）。
         */
        public int subChunk1Size;
        /**
         * PCM = 1 （比如，线性采样），如果是其它值的话，则可能是一些压缩形式
         */
        public short audioFormat;
        /**
         * 1 => 单声道  |  2 => 双声道
         */
        public short numChannels;
        /**
         * 采样率
         */
        public int sampleRate;
        /**
         * 等于： SampleRate * numChannels * bitsPerSample / 8
         */
        public int byteRate;

        /**
         * 等于：NumChannels * bitsPerSample / 8
         */
        public short blockAlign;
        /**
         * 采样分辨率，也就是每个样本用几位来表示，一般是 8bits 或是 16bits
         */
        public short bitsPerSample;
        /**
         * 新数据块，真正的声音数据
         * ASCII 码表示的“data ”——最后是一个空格。（0x64617461）
         */
        public char[] subChunk2ID = {'d', 'a', 't', 'a'};

        /**
         * 数据大小，即，其后跟着的采样数据的大小。
         */
        public int subChunk2Size;

        public byte[] getHeader() throws IOException {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            writeChar(bos, chunkID);
            writeInt(bos, chunkSize);
            writeChar(bos, format);
            writeChar(bos, subChunk1ID);
            writeInt(bos, subChunk1Size);
            writeShort(bos, audioFormat);
            writeShort(bos, numChannels);
            writeInt(bos, sampleRate);
            writeInt(bos, byteRate);
            writeShort(bos, blockAlign);
            writeShort(bos, bitsPerSample);
            writeChar(bos, subChunk2ID);
            writeInt(bos, subChunk2Size);
            bos.flush();
            byte[] r = bos.toByteArray();
            bos.close();
            return r;
        }

        private void writeShort(ByteArrayOutputStream bos, int s) throws IOException {
            byte[] mybyte = new byte[2];
            mybyte[1] = (byte) ((s << 16) >> 24);
            mybyte[0] = (byte) ((s << 24) >> 24);
            bos.write(mybyte);
        }


        private void writeInt(ByteArrayOutputStream bos, int n) throws IOException {
            byte[] buf = new byte[4];
            buf[3] = (byte) (n >> 24);
            buf[2] = (byte) ((n << 8) >> 24);
            buf[1] = (byte) ((n << 16) >> 24);
            buf[0] = (byte) ((n << 24) >> 24);
            bos.write(buf);
        }

        private void writeChar(ByteArrayOutputStream bos, char[] id) {
            for (int i = 0; i < id.length; i++) {
                char c = id[i];
                bos.write(c);
            }
        }

    }
}


