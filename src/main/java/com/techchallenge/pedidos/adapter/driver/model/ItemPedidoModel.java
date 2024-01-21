package com.techchallenge.pedidos.adapter.driver.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemPedidoModel {
    private BigDecimal precoTotal;
    private Integer quantidade;
    private ProdutoModel produto;

    public BigDecimal getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(BigDecimal precoTotal) {
        this.precoTotal = precoTotal;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public ProdutoModel getProduto() {
        return produto;
    }

    public void setProduto(ProdutoModel produto) {
        this.produto = produto;
    }

	@Override
	public int hashCode() {
		return Objects.hash(precoTotal, produto, quantidade);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemPedidoModel other = (ItemPedidoModel) obj;
		return Objects.equals(precoTotal, other.precoTotal) && Objects.equals(produto, other.produto)
				&& Objects.equals(quantidade, other.quantidade);
	}
}
