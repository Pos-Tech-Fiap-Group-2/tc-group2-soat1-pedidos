package com.techchallenge.pedidos.core.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.gateways.ItemPedidoGateway;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;

@RunWith(SpringJUnit4ClassRunner.class)
public class ItemPedidoUseCaseTest {

	@InjectMocks
	private ItemPedidoUseCase useCase;
	@Mock
    private ItemPedidoGateway gateway;
	
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
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
    
	private ItemPedido createItemPedido(Long id, Pedido pedido, BigDecimal precoTotal, Integer quantidade) {
		ItemPedido itemPedido = new ItemPedido();
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		
		itemPedido.setId(id);
		itemPedido.setPedido(pedido);
		itemPedido.setPrecoTotal(precoTotal);
		itemPedido.setProduto(produto);
		itemPedido.setQuantidade(quantidade);
		
		return itemPedido;
	}
	
    private Pedido createPedido() {
    	Pedido pedido = new Pedido();
    	pedido.setId(1L);
    	
    	return pedido;
    }
	
    @Test
    public void adicionarItemAoPedido() {
    	Pedido pedido = createPedido();
    	ItemPedido entity = createItemPedido(1L, pedido, new BigDecimal("25.99").multiply(BigDecimal.valueOf(2L)), 2);
    	ItemPedido expected = createItemPedido(1L, pedido, new BigDecimal("25.99").multiply(BigDecimal.valueOf(2L)), 2);
    	
    	
    	when(this.gateway.adicionarItemAoPedido(pedido.getId(), entity)).thenReturn(expected);
    	assertEquals(this.useCase.adicionarItemAoPedido(pedido.getId(), entity), expected);
    }
    
    @Test
    public void atualizarItemAoPedido() {
    	Pedido pedido = createPedido();
    	ItemPedido entity = createItemPedido(1L, pedido, new BigDecimal("25.99"), 1);
    	
    	doNothing().when(this.gateway).atualizarItemAoPedido(pedido.getId(), entity);
    	assertDoesNotThrow(() -> this.useCase.atualizarItemAoPedido(pedido.getId(), entity));
    }
    
    @Test
    public void excluirItemAoPedido() {
    	Pedido pedido = createPedido();
    	Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	
    	doNothing().when(this.gateway).excluirItemAoPedido(pedido.getId(), produto.getId());
    	assertDoesNotThrow(() -> this.useCase.excluirItemAoPedido(pedido.getId(), produto.getId()));
    }
}
