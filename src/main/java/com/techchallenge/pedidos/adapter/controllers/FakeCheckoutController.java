package com.techchallenge.pedidos.adapter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pedidos.adapter.driver.model.PedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.PedidoInput;
import com.techchallenge.pedidos.adapter.mapper.api.PedidoApiMapper;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.usecases.FakeCheckoutUseCase;

@Component
public class FakeCheckoutController {
	
    @Autowired
    private FakeCheckoutUseCase useCase;
    
    @Autowired
    private PedidoApiMapper pedidoMapper;
	
	public PedidoModel checkout(PedidoInput pedidoInput) {
		
		Pedido pedido = pedidoMapper.toDomainObject(pedidoInput);
		return pedidoMapper.toModel(useCase.checkout(pedido));
	}
}
