package com.techchallenge.pedidos.core.domain.usecases;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;



@Service
public class FakeCheckoutUseCase {
	
	@Autowired
	private PedidoUseCase pedidoUseCase;
	@Autowired
	private ProdutoUseCase produtoUseCase;
	@Autowired
	private ClienteUseCase clienteUseCase;

	@Autowired
	private ProducaoUseCase producaoUseCase;

	@Autowired
	private PagamentoUseCase pagamentoUseCase;
	
	public Pedido checkout(Pedido pedido) {
		
		this.validarItens(pedido);
		this.validarCliente(pedido);
		
		pedido.setDataSolicitacao(OffsetDateTime.now());
		pedido.setStatus(StatusPedido.RECEBIDO);
		pedido.calcularValor();

		Pedido pedidoCadastrado = pedidoUseCase.gravar(pedido);

		if(pedidoCadastrado != null) {
			producaoUseCase.enviarPedidoParaProducao(pedidoCadastrado.getId());
			pagamentoUseCase.enviarPedidoParaPagamento(pedidoCadastrado.getId());
		}

		return pedidoCadastrado;
	}
	
	private void validarItens(Pedido pedido) {
	    pedido.getItens().forEach(item -> {
	    	Long produtoId = item.getProduto().getId();
	    	
	    	Produto produto = produtoUseCase.buscarPorId(produtoId);
	    	
	        item.setPedido(pedido);
	        item.setProduto(produto);
	        item.calcularPrecoTotal();
	    });
	}
	
	private void validarCliente(Pedido pedido) {
		Cliente cliente = clienteUseCase.buscarPorId(pedido.getCliente().getId());
		pedido.setCliente(cliente);
	}
}
