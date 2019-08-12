package com.myself.tomcat.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 14:30.
 **/
public class Response {

    private OutputStream outputStream;

    public static final String responseHeader="HTTP/1.1 200 \r\n"
            + "Content-Type: text/html\r\n"
            + "\r\n";

    public Response(OutputStream outputStream) throws IOException {
        this.outputStream= outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
