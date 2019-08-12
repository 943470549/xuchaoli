package com.myself.tomcat.servlet;

import com.myself.tomcat.http.Request;
import com.myself.tomcat.http.Response;

import java.io.OutputStream;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 14:33.
 **/
public class LoginServlet extends Servlet {

    @Override
    public void doGet(Request request, Response response) {
        this.doPost(request, response);
    }

    @Override
    public void doPost(Request request, Response response) {
        try {
            OutputStream outputStream = response.getOutputStream();
            String res = Response.responseHeader + "Hello,welcome to here !";
            System.out.println(res);
            outputStream.write(res.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
