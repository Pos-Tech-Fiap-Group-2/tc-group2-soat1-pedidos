package com.techchallenge.pedidos.core.domain.usecases;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.gateways.PedidoGateway;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;

@RunWith(SpringJUnit4ClassRunner.class)
public class PedidoUseCaseTest {

	@InjectMocks
	private PedidoUseCase useCase;
	@Mock
    private PedidoGateway gateway;
	
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
    
	private Cliente createCliente(Long id, Long cpf, String email, String nome) {
		Cliente cliente = new Cliente();
		
		cliente.setCpf(cpf);
		cliente.setEmail(email);
		cliente.setId(id);
		cliente.setNome(nome);
		
		return cliente;
	}
	
	private Categoria createCategoria(Long id, String nome) {
		Categoria categoria = new Categoria();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
	}
	
	private Produto createProduto(String descricao, Long id, String imagem, String nome, BigDecimal preco) {
		Produto produto = new Produto();
		
		produto.setCategoria(createCategoria(1L, "Lanche"));
		produto.setDescricao(descricao);
		produto.setId(id);
		produto.setImagem(imagem);
		produto.setNome(nome);
		produto.setPreco(preco);
		
		return produto;
	}
	
	private ItemPedido createItemPedido(Long id, Pedido pedido, BigDecimal precoTotal, Produto produto, Integer quantidade) {
		ItemPedido itemPedido = new ItemPedido();
		
		itemPedido.setId(id);
		itemPedido.setPedido(pedido);
		itemPedido.setPrecoTotal(precoTotal);
		itemPedido.setProduto(produto);
		itemPedido.setQuantidade(quantidade);
		
		return itemPedido;
	}
	
	@SuppressWarnings("serial")
	private List<ItemPedido> createItensPedido(Pedido pedido) {
		
		return new ArrayList<ItemPedido>() {{
			this.add(createItemPedido(1L, pedido, new BigDecimal("25.99"), createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99")), 2));
			this.add(createItemPedido(1L, pedido, new BigDecimal("20.99"), createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger duplo", new BigDecimal("20.99")), 1));
		}};
	}
	
    private Pedido createPedido(Long id, Cliente cliente) {
    	return createPedido(id, cliente, StatusPedido.RECEBIDO);
    }
    
    private Pedido createPedido(Long id, Cliente cliente, StatusPedido status) {
    	Pedido pedido = new Pedido();
    	
    	pedido.setCliente(cliente);
    	pedido.setId(id);
    	pedido.setItens(createItensPedido(pedido));
    	pedido.setStatus(status);
    	
    	return pedido;
    }
    
    @Test
    public void buscarPedidos() {
    	List<Pedido> pedidos = new ArrayList<>();
    	pedidos.add(createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste")));
    	pedidos.add(createPedido(2L, createCliente(2L, 12345678902L, "cliente.teste2@teste.com.br", "Cliente Teste 2")));
    	
    	when(this.gateway.buscarPedidos()).thenReturn(pedidos);
    	assertTrue(this.useCase.buscarPedidos().size() > 0);
    }

    @Test
    public void buscarPedidoPorId() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	Pedido entity = createPedido(1L, cliente);
    	Pedido expected = createPedido(1L, cliente);
    	
    	when(this.gateway.buscarPedidoPorId(entity.getId())).thenReturn(expected);
    	assertEquals(this.useCase.buscarPedidoPorId(entity.getId()), expected);
    }

    @Test
    public void buscarPedidosPorStatus() {
    	List<Pedido> pedidos = new ArrayList<>();
    	pedidos.add(createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste")));
    	pedidos.add(createPedido(2L, createCliente(2L, 12345678902L, "cliente.teste2@teste.com.br", "Cliente Teste 2")));
    	
    	when(this.gateway.buscarPedidosPorStatus(StatusPedido.RECEBIDO)).thenReturn(pedidos);
    	assertTrue(this.useCase.buscarPedidosPorStatus(StatusPedido.RECEBIDO).size() > 0);
    }
    
    @Test
    public void buscarPedidoPorIdEStatus() {
    	Pedido entity = this.createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"), StatusPedido.PREPARACAO);
    	Pedido expected = this.createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"), StatusPedido.PREPARACAO);
    	
    	when(this.gateway.buscarPedidoPorIdEStatus(entity.getId(), entity.getStatus())).thenReturn(expected);
    	assertEquals(this.useCase.buscarPedidoPorIdEStatus(entity.getId(), entity.getStatus()), expected);
    }

    @Test
    public void atualizarStatusDoPedido() {
    	Pedido entity = this.createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"), StatusPedido.PREPARACAO);
    	
    	doNothing().when(this.gateway).atualizarStatusDoPedido(entity, StatusPedido.PRONTO);
    	assertDoesNotThrow(() -> this.useCase.atualizarStatusDoPedido(entity, StatusPedido.PRONTO));
    }
    
    @Test
    public void gravar() {
    	Pedido entity = this.createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"), StatusPedido.RECEBIDO);
    	Pedido expected = this.createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"), StatusPedido.RECEBIDO);
    	
    	when(this.gateway.gravar(entity)).thenReturn(expected);
    	assertEquals(this.useCase.gravar(entity), expected);
    }
    
    @Test
    public void excluir() {
    	Pedido entity = this.createPedido(1L, createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"), StatusPedido.RECEBIDO);
    	
    	doNothing().when(this.gateway).excluir(entity.getId());
    	assertDoesNotThrow(() -> this.useCase.excluir(entity.getId()));
    }
}
