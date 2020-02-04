package io.mozaiko.io.mozaiko.server;

import static io.mozaiko.io.mozaiko.util.Constants.API_CREATE;
import static io.mozaiko.io.mozaiko.util.Constants.API_LIST_ALL;
import static io.mozaiko.io.mozaiko.util.Constants.API_ROUTE;

import io.mozaiko.io.mozaiko.Run;
import io.mozaiko.io.mozaiko.service.ProdutoService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.validation.ValidationException;
import io.vertx.ext.web.handler.BodyHandler;

public class ServerVerticle extends AbstractVerticle {
	
	private Run run = new Run();	
	private ProdutoService service = new ProdutoService();	

	@Override
	public void start(Future<Void> future) {
		
		Router router = setRouters();

		createListen(future, router);
	}
	
	private Router setRouters() {

		Router router = Router.router(vertx);
		
		router.route(API_ROUTE).handler(BodyHandler.create());
		router.post(API_CREATE).handler(service::adicionarProduto).failureHandler(this::handlerValidationException);
		router.get(API_LIST_ALL).handler(service::getProdutos);
		
		return router;
	}
	
	private void createListen(Future<Void> future, Router router) {

		vertx
			.createHttpServer()
			.requestHandler(router::accept)
			.listen(config().getInteger("http.port", 8080),
				result -> {
					if (result.succeeded()) {
						future.complete();
					} else {
						future.fail(result.cause());
					}
				}
			);
	}
	
	private void handlerValidationException(RoutingContext routingContext) {
		
		Throwable failure = routingContext.failure();
		if (failure instanceof ValidationException) {
			run.routing(routingContext, failure.getMessage());
		}
	}	
	
	
}
