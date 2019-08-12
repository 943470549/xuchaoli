package com.myself.tomcat.servlet;

import com.myself.tomcat.http.Request;
import com.myself.tomcat.http.Response;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 14:32.
 **/
public abstract class Servlet {

    public void service(Request request, Response response) {

        //判断是调用doget 还是 dopost
        if ("get".equalsIgnoreCase(request.getMethod())) {
            this.doGet(request, response);
        } else {
            this.doPost(request, response);
        }

    }

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);

}
