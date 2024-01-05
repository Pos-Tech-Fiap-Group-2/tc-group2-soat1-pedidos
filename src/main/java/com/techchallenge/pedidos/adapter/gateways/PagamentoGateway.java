package com.techchallenge.pedidos.adapter.gateways;

import java.util.List;

import com.techchallenge.pedidos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pedidos.adapter.dto.pagamentos.PagamentoResponseDTO;
import com.techchallenge.pedidos.core.domain.entities.TipoPagamento;


public interface PagamentoGateway {

	PagamentoPixResponseDTO efetuarPagamento(Long pedidoId, TipoPagamento tipoPagamento);
	PagamentoResponseDTO consultarPagamento(Long paymentId);
	List<TipoPagamento> listar();
}
