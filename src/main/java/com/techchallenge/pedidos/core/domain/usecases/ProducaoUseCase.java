package com.techchallenge.pedidos.core.domain.usecases;

import com.techchallenge.pedidos.core.domain.Interfaces.ProducaoClient;
import com.techchallenge.pedidos.core.domain.entities.Pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducaoUseCase {

    private final ProducaoClient producaoClient;

    @Autowired
    public ProducaoUseCase(ProducaoClient producaoClient) {
        this.producaoClient = producaoClient;
    }
    public void enviarPedidoParaProducao(Long id) {
        this.producaoClient.adicionarProducao(id);
    }
}
