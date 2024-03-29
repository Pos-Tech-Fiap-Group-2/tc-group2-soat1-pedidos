package com.techchallenge.pedidos.adapter.mapper.api;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.techchallenge.pedidos.adapter.driver.model.PedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.PedidoInput;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;

@Component
public class PedidoApiMapper {

	@Autowired
	private ModelMapper mapper;
	
    public Pedido toDomainObject(PedidoInput input) {
        Pedido pedido = mapper.map(input, Pedido.class);

        pedido.setCliente(new Cliente());
        pedido.getCliente().setId(input.getClienteId());

        return pedido;
    }

    public PedidoModel toModel(Pedido pedido) {
        return mapper.map(pedido, PedidoModel.class);
    }

    public Collection<PedidoModel> toCollectionModel(Collection<Pedido> pedidos) {
        return pedidos.stream()
                .map(c -> mapper.map(c, PedidoModel.class))
                .collect(Collectors.toList());
    }

    public Collection<PedidoModel> toCollectionModelOrderByStatus(Collection<Pedido> pedidos) {
        return pedidos.stream()
                .map(c -> mapper.map(c, PedidoModel.class)).sorted(Comparator.comparing(PedidoModel::getStatus))
                .filter(pedido -> pedido.getStatus() != StatusPedido.FINALIZADO)
                .collect(Collectors.toList());
    }
}
