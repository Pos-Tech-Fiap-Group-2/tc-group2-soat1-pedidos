package com.techchallenge.pedidos.adapter.driver.model;

import java.util.Objects;

public class CategoriaResumoModel {

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoriaResumoModel other = (CategoriaResumoModel) obj;
		return Objects.equals(nome, other.nome);
	}
}
