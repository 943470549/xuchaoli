package com.myself.tomcat.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 14:08.
 **/
public class TomcatV01 {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8082);
        System.out.println("Tomcat(v0.1)服务已启动,端口号:8082");
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            System.out.println("接受到请求");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String msg = null;
            while ((msg = reader.readLine()) != null) {
                if(msg.length() == 0) {
                }
                System.out.println(msg);
            }
            String res = "来自Tomcat(v0.1)的回复";
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(res.getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();
            socket.close();
        }
    }
}
