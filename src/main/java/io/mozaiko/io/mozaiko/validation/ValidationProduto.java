package io.mozaiko.io.mozaiko.validation;

import io.mozaiko.io.mozaiko.entity.Produto;
import io.vertx.ext.web.api.validation.ValidationException;

public class ValidationProduto {
	
	public void validate(Produto produto) {

		if (produto.getNome() == null) {			
			handlerValidationException("Nome do produto é obrigatório!");
		}
		
		if (produto.getCodigoBarras() == null) {			
			handlerValidationException("Código de barras do produto é obrigatório!");
		}
		
		if (produto.getNumeroSerie() == null) {			
			handlerValidationException("Número de série do produto é obrigatório!");
		}
	}
	
	public void handlerValidationException(String mensagem) {
		throw ValidationException.ValidationExceptionFactory.generateNotMatchValidationException(mensagem);
	}
}
