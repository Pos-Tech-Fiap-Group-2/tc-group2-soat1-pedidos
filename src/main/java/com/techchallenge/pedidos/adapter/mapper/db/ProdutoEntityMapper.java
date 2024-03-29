package com.techchallenge.pedidos.adapter.mapper.db;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.drivers.db.entities.ProdutoEntity;

@Component
public class ProdutoEntityMapper {

    @Autowired
    private ModelMapper mapper;

    public ProdutoEntity toModel(Produto produto) {
        return mapper.map(produto, ProdutoEntity.class);
    }
}
