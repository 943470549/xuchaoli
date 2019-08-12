package com.myself.tomcat.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * TODO
 * Created by zhaozhongchao on 2019/3/21 11:35.
 **/
public class NioTomcat {

    private Selector selector;

    // 初始化方法，selector绑定端口和ip
    public void init() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(8081));
        // 设定非阻塞
        serverSocketChannel.configureBlocking(false);
        System.out.println("NIO服务已启动,端口是:8081");
//        ServerSocket serverSocket = serverSocketChannel.socket();
        // 需要接受请求:把这个接受时间注册到服务器的通道上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws IOException {
        while (true) {
            // 如果有事件或请求过来，阻塞
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove(); // 删除,避免死循环
                if(selectionKey.isAcceptable()) { // 请求事件
                    accept(selectionKey);
                } else if(selectionKey.isReadable()) { // 读事件
                    read(selectionKey);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        //事件中传过来的,key我们把这个通道拿到
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        //把这个设置为非阻塞
        channel.configureBlocking(false);
        //注册读事件
        channel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        //创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel channel = (SocketChannel)key.channel();
        //我们把通道的数据填入缓冲区
        channel.read(buffer);
        String request = new String(buffer.array()).trim();
        System.out.println("客户端的请求内容" + request);
        //把我们的html内容返回给客户端

        String outString = "HTTP/1.1 200 OK\n"
                +"Content-Type:text/html; charset=UTF-8\n\n"
                +"<html>\n"
                +"<head>\n"
                +"<title>first page</title>\n"
                +"</head>\n"
                +"<body>\n"
                +"hello fomcat\n"
                +"</body>\n"
                +"</html>";

        ByteBuffer outBuffer = ByteBuffer.wrap(outString.getBytes());
        channel.write(outBuffer);
        channel.close();
    }

    public static void main(String[] args) {
        NioTomcat server = new NioTomcat();
        try {
            server.init();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
