package com.techchallenge.pedidos.adapter.gateways;

import com.techchallenge.pedidos.core.domain.entities.ItemPedido;

public interface ItemPedidoGateway {

    ItemPedido adicionarItemAoPedido(Long pedidoId, ItemPedido itemPedido);
    void atualizarItemAoPedido(Long pedidoId, ItemPedido itemPedido);
    void excluirItemAoPedido(Long pedidoId, Long produtoId);
}
