package io.mozaiko.io.mozaiko.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import io.mozaiko.io.mozaiko.Run;
import io.mozaiko.io.mozaiko.entity.Produto;
import io.mozaiko.io.mozaiko.validation.ValidationProduto;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class ProdutoService {

	private static final AtomicInteger INCREMENT = new AtomicInteger();
	
	private Set<Produto> produtos = new HashSet<>();
	private ValidationProduto validationProduto = new ValidationProduto();
	private Run run = new Run();
	
	public void adicionarProduto(RoutingContext routingContext) {
        
		final Produto produto = Json.decodeValue(routingContext.getBodyAsString(), Produto.class);

		validationProduto.validate(produto);
		
		adicionarProduto(produto);
		
		run.routing(routingContext, produto);
	}	

	private void adicionarProduto(Produto produto) {
				
		if (produtos.isEmpty()) {
			adicionar(produto);
		} else {

			Predicate<Produto> filter = find -> find.getCodigoBarras().equals(produto.getCodigoBarras());
			
			Produto found = produtos.stream().filter(filter).findAny().orElse(null);
			
			if (found == null) {			
				adicionar(produto);
			} else if (!found.getNumeroSerie().equals(produto.getNumeroSerie())){
				adicionar(produto);
			} else {
				validationProduto.handlerValidationException("Produto já adicionado em estoque!");
			}
		}
	}
	
	private void adicionar(Produto produto) {
		produto.setId(setId());
		produtos.add(produto);		
	}
	
	public void getProdutos(RoutingContext routingContext) {
		run.routing(routingContext, produtos.toArray());
	}
	
	public Integer setId() {
		 return INCREMENT.getAndIncrement() + 1;
	}
}
