package krq;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        Vertx vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions().setWorker(false)
                .setInstances(2)
                .setHa(true);

        Future<String> future2 = vertx.deployVerticle("krq.TestVertical2", options);
        Future<String> future = vertx.deployVerticle("krq.TestVertical", options);
       future.setHandler(res -> {
            if (res.succeeded()){
                System.out.println(res.result());
            }else {
                System.out.println(res.cause());
            }
        });
        future2.setHandler(res -> {
            if (res.succeeded()){
                System.out.println(res.result());
            }else {
                System.out.println(res.cause());
            }
        });
    }
    public void test(){

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .handler(new LoggingHandler(LogLevel.WARN))
                    .channel(NioServerSocketChannel.class);
//                    .childHandler(new MyServerInializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
