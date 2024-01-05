package com.techchallenge.pedidos.adapter.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pedidos.adapter.driver.model.TipoPagamentoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.EventoPagamentoInput;
import com.techchallenge.pedidos.adapter.driver.model.input.TipoPagamentoInput;
import com.techchallenge.pedidos.adapter.dto.pagamentos.PagamentoPixResponseDTO;
import com.techchallenge.pedidos.adapter.mapper.api.MercadoPagoApiMapper;
import com.techchallenge.pedidos.adapter.mapper.api.PagamentoApiMapper;
import com.techchallenge.pedidos.core.domain.entities.EventoPagamento;
import com.techchallenge.pedidos.core.domain.entities.TipoPagamento;
import com.techchallenge.pedidos.core.domain.usecases.PagamentoUseCase;

@Component
public class PagamentoController {
	
    @Autowired
    private PagamentoUseCase useCase;
    
    @Autowired
    private PagamentoApiMapper mapper;

	@Autowired
	private MercadoPagoApiMapper mercadoPagoApiMapper;
	
	public PagamentoPixResponseDTO realizarPagamento(Long pedidoId, TipoPagamentoInput tipoPagamentoInput) {
		TipoPagamento tipoPagamento = mapper.toDomainObject(tipoPagamentoInput);
		PagamentoPixResponseDTO pagamentoPixResponseDTO = useCase.efetuarPagamento(pedidoId, tipoPagamento);

		return mercadoPagoApiMapper.toDomainObject(pagamentoPixResponseDTO);
	}
	
	public Collection<TipoPagamentoModel> listar() {
		return mapper.toCollectionModel(useCase.listar());
	}

	public void confirmarPagamento(EventoPagamentoInput eventoPagamentoInput) {
		EventoPagamento eventoPagamento = mapper.toDomainObject(eventoPagamentoInput);
		Long paymentId = eventoPagamento.getData().getId();

		mercadoPagoApiMapper.toDomainObject(useCase.consultarPagamento(paymentId));
	}
}