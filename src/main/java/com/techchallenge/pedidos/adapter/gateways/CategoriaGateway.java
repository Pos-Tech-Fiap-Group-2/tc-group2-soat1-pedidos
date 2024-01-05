package com.techchallenge.pedidos.adapter.gateways;

import java.util.List;

import com.techchallenge.pedidos.core.domain.entities.Categoria;



public interface CategoriaGateway {

	List<Categoria> buscarTodos();
	Categoria buscarPorId(Long id);
}
