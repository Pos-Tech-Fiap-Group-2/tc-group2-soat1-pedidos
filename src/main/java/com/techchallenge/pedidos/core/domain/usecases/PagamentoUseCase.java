package com.techchallenge.pedidos.core.domain.usecases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techchallenge.pedidos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pedidos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pedidos.adapter.gateways.PagamentoGateway;
import com.techchallenge.pedidos.adapter.gateways.PedidoGateway;
import com.techchallenge.pedidos.core.domain.entities.*;

@Service
public class PagamentoUseCase {
	
	@Autowired
	private PagamentoGateway gateway;

	public PagamentoPixResponseDTO efetuarPagamento(Long pedidoId, TipoPagamento tipoPagamento) {
		return gateway.efetuarPagamento(pedidoId, tipoPagamento);
	}
	
	public PagamentoResponseDTO consultarPagamento(Long paymentId) {
		return gateway.consultarPagamento(paymentId);
	}
	
	public List<TipoPagamento> listar() {
		return gateway.listar();
	}
}