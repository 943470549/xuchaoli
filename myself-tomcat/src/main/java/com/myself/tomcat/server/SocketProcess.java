package com.myself.tomcat.server;

import com.myself.tomcat.http.Request;
import com.myself.tomcat.http.Response;
import com.myself.tomcat.servlet.Servlet;

import java.io.OutputStream;
import java.net.Socket;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 14:35.
 **/
public class SocketProcess extends Thread {

    protected Socket socket;

    public SocketProcess(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Request request = new Request(socket.getInputStream());
            Response response = new Response(socket.getOutputStream());

            Servlet servlet = MyTomcat.servletMapping.get(request.getUrl());

            if (servlet != null) {
                servlet.service(request, response);
            } else {
                String res = Response.responseHeader + "Hello World";
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(res.getBytes());
                outputStream.flush();
                outputStream.close();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
