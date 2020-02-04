package io.mozaiko.io.mozaiko;

import io.mozaiko.io.mozaiko.server.ServerVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class Run {

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new ServerVerticle());
		System.out.println("Vertx iniciado e rodando na porta 8080");
	}
	
	public void routing(RoutingContext routingContext, Object object) {
		routingContext
			.response()
			.putHeader("content-type", "application/json; charset=utf-8")
			.end(Json.encodePrettily(object));
	}
}
