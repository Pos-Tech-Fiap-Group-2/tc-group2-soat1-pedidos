package com.techchallenge.pedidos.adapter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.techchallenge.pedidos.adapter.driver.model.PedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.PedidoInput;
import com.techchallenge.pedidos.adapter.mapper.api.PedidoApiMapper;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.usecases.FakeCheckoutUseCase;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Component
public class FakeCheckoutController {
	
    @Autowired
    private FakeCheckoutUseCase useCase;
    
    @Autowired
    private PedidoApiMapper pedidoMapper;
	
	@ApiOperation("Efetuar checkout do pedido na plataforma")
	@ApiResponses({ 
			@ApiResponse(code = 200, message = "Checkout realizado com sucesso"),
			@ApiResponse(code = 400, message = "Caso o pedido não tenha itens associados"),
			@ApiResponse(code = 404, message = "Caso o pedido com o ID informado não exista")
			})
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public PedidoModel checkout(@RequestBody PedidoInput pedidoInput) {
		
		Pedido pedido = pedidoMapper.toDomainObject(pedidoInput);
		return pedidoMapper.toModel(useCase.checkout(pedido));
	}
}
