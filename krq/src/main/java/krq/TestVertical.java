package krq;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class TestVertical extends AbstractVerticle {

    private HttpServer server;

    @Override
    public void start(Promise<Void> startFuture) {
        server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        Route route = router.route("/test");
        route.handler(context ->{
            HttpServerResponse response = context.response();
            response.end("test1");
        });

        // Now bind the server:
        server.requestHandler(router).listen(8090, res -> {
            if (res.succeeded()) {
                startFuture.complete();
            } else {
                startFuture.fail(res.cause());
            }
        });

    }
}
