package com.myself.tomcat.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 14:23.
 **/
public class Request {

    private String method;

    private String url;

    public Request() {

    }

    public Request(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String msg = reader.readLine();
        String[] input = msg.split(" ");
        this.method = input[0];
        this.url = input[1];
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
