package com.dingjianjun.basetech.nio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author : Jianjun.Ding
 * @description:
 * @date 2020/6/23
 */
public class HttpTest {
    private static final String SEPERATOR = "\r\n";
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(8888);
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));){

            String line;
            while (!"".equals(line = br.readLine())) {
                System.out.println(line);
            }

            StringBuilder sb = new StringBuilder(128);
            sb.append("HTTP/1.1 200 OK").append(SEPERATOR)
                    .append("Content-Type: application/json;charset=utf-8")
                    .append(SEPERATOR).append(SEPERATOR)
                    .append("{{\"name\":\"xxoo\"}}");


            bw.write(sb.toString());

        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);

        }
    }
}
