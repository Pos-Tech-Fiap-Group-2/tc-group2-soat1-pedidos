package com.techchallenge.pedidos.adapter.driver.model.input;

public class ItemPedidoInput {

	private Long itemPedidoId;
	
	private Integer quantidade;

	private Long produtoId;

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Long getProdutoId() {
		return produtoId;
	}

	public void setProdutoId(Long produtoId) {
		this.produtoId = produtoId;
	}

	public Long getItemPedidoId() {
		return itemPedidoId;
	}

	public void setItemPedidoId(Long itemPedidoId) {
		this.itemPedidoId = itemPedidoId;
	}
}
