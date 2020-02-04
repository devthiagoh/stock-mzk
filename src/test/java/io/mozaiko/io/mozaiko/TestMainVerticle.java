package io.mozaiko.io.mozaiko;

import java.io.IOException;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.mozaiko.io.mozaiko.entity.Produto;
import io.mozaiko.io.mozaiko.server.ServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class TestMainVerticle {

    private Vertx vertx;

    private int port = 8080;

    @BeforeClass
    public static void beforeClass() {

    }

    @Before
    public void setup(TestContext testContext) throws IOException {
        vertx = Vertx.vertx();

        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));

        vertx.deployVerticle(ServerVerticle.class.getName(), options, testContext.asyncAssertSuccess());
    }

    @After
    public void tearDown(TestContext testContext) {
        vertx.close(testContext.asyncAssertSuccess());
    }

    @Test
	public void givenId_whenReceivedArticle_thenSuccess(TestContext context) {
		final Async async = context.async();

		Produto produto = new Produto();
		produto.setId(1);
		produto.setNome("Produto 01");
		produto.setCodigoBarras("7890000000001");
		produto.setNumeroSerie(0001);

		final String json = Json.encodePrettily(produto);
		final String length = Integer.toString(json.length());
		Vertx vertx = Vertx.vertx();
		vertx.createHttpClient().post(port, "localhost", "/api/produtos").putHeader("content-type", "application/json")
				.putHeader("content-length", length).handler(response -> {
					context.assertEquals(response.statusCode(), 200);
					context.assertTrue(response.headers().get("content-type").contains("application/json"));
					response.bodyHandler(body -> {
						final Produto result = Json.decodeValue(body.toString(), Produto.class);
						context.assertEquals(result.getNome(), "Produto 01");
						context.assertEquals(result.getCodigoBarras(), "7890000000001");
						context.assertEquals(result.getNumeroSerie(), 0001);
						context.assertNotNull(result.getId());
						async.complete();
					});
				}).write(json).end();
	}

}
