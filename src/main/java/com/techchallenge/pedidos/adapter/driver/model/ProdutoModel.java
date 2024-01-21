package com.techchallenge.pedidos.adapter.driver.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ProdutoModel {

	private Long id;
    private String nome;
    private CategoriaResumoModel categoria;

    private BigDecimal preco;

    private String descricao;

    private String imagem;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public CategoriaResumoModel getCategoria() {return categoria;}

    public void setCategoria(CategoriaResumoModel categoria) {this.categoria = categoria;}

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdutoModel other = (ProdutoModel) obj;
		return Objects.equals(id, other.id);
	}
}
