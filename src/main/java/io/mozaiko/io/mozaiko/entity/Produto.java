package io.mozaiko.io.mozaiko.entity;

import java.io.Serializable;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String codigoBarras;
	private Integer numeroSerie;
	
	public Produto() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public Integer getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(Integer numeroSerie) {
		this.numeroSerie = numeroSerie;
	}
}
