package com.techchallenge.pedidos.core.domain.usecases;

import com.techchallenge.pedidos.core.domain.Interfaces.PagamentoClient;
import org.springframework.stereotype.Service;

@Service
public class PagamentoUseCase {
    private final PagamentoClient pagamentoClient;

    public PagamentoUseCase(PagamentoClient pagamentoClient) {
        this.pagamentoClient = pagamentoClient;
    }

    public void enviarPedidoParaPagamento(Long id) {
        this.pagamentoClient.enviarPedidoParaPagamento(id);
    }
}
