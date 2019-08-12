package com.myself.tomcat.server;

import com.myself.tomcat.servlet.Servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 11:35.
 **/
public class MyTomcat {

    private static final int port = 8099;
    private static final Properties properties = new Properties();
    public static final HashMap<String, Servlet> servletMapping = new HashMap<>();

    public void init() {

        InputStream io = null;

        String basePath;

        try {
            //获取basePath
            basePath = MyTomcat.class.getResource("/").getPath();
            System.out.println(basePath);
            io = new FileInputStream(basePath + "web.properties");
            properties.load(io);
            io.close();

            //初始化ServletMapping
            //返回属性key的集合
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                if (key.toString().contains("url")) {
                    System.out.println(key.toString() + "=" + properties.get(key));
                    //根据key值获取className
                    Object classname = properties.get(key.toString().replace("url", "classname"));
                    servletMapping.put(properties.get(key.toString()).toString(), (Servlet) Class.forName(classname.toString()).newInstance());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (io != null) {
                try {
                    io.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Tomcat 服务已启动，地址：localhost ,端口：" + port);
            //持续监听
            do {
                Socket socket = serverSocket.accept();
                //处理任务
                Thread thread = new SocketProcess(socket);
                thread.start();
            } while (true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        MyTomcat tomcat = new MyTomcat();
        tomcat.init();
        tomcat.start();
    }
}
