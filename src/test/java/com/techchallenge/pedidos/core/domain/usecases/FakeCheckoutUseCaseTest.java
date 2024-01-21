package com.techchallenge.pedidos.core.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;

@RunWith(SpringJUnit4ClassRunner.class)
public class FakeCheckoutUseCaseTest {

	@InjectMocks
	private FakeCheckoutUseCase useCase;
	
	@Mock
	private PedidoUseCase pedidoUseCase;
	@Mock
	private ProdutoUseCase produtoUseCase;
	@Mock
	private ClienteUseCase clienteUseCase;
	
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
    
    private Pedido createPedido() {
    	Pedido pedido = new Pedido();
    	
    	pedido.setCliente(createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"));
    	pedido.setId(1L);
    	pedido.setItens(createItensPedido(pedido));
    	pedido.setStatus(StatusPedido.RECEBIDO);
    	
    	return pedido;
    }
    
    @Test
	public void checkout() {
    	
    	Pedido expected = createPedido();
    	Pedido pedido = createPedido();
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	
    	List<ItemPedido> itens = createItensPedido(pedido);
    	
    	itens.forEach(item -> {
    		when(produtoUseCase.buscarPorId(item.getProduto().getId())).thenReturn(item.getProduto());
    	});
    	
    	when(clienteUseCase.buscarPorId(cliente.getId())).thenReturn(cliente);
		when(pedidoUseCase.gravar(pedido)).thenReturn(expected);
		
		assertEquals(useCase.checkout(pedido), expected);
	}
}
