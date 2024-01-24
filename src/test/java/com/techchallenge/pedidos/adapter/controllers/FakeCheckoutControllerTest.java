package com.techchallenge.pedidos.adapter.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.driver.model.CategoriaResumoModel;
import com.techchallenge.pedidos.adapter.driver.model.ClienteModel;
import com.techchallenge.pedidos.adapter.driver.model.ItemPedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.PedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.ProdutoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ItemPedidoInput;
import com.techchallenge.pedidos.adapter.driver.model.input.PedidoInput;
import com.techchallenge.pedidos.adapter.mapper.api.PedidoApiMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;
import com.techchallenge.pedidos.core.domain.usecases.FakeCheckoutUseCase;

@RunWith(SpringJUnit4ClassRunner.class)
public class FakeCheckoutControllerTest {

	@InjectMocks
	private FakeCheckoutController controller;
	
    @Mock
    private FakeCheckoutUseCase useCase;
    @Mock
    private PedidoApiMapper pedidoMapper;
    
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
	
    private PedidoModel createPedidoModel(Pedido pedido) {
    	PedidoModel model = new PedidoModel();
    	
    	model.setCliente(createClienteModel(pedido.getCliente()));
    	model.setDataCancelamento(pedido.getDataCancelamento());
    	model.setDataFinalizacao(pedido.getDataFinalizacao());
    	model.setDataSolicitacao(pedido.getDataSolicitacao());
    	model.setId(pedido.getId());
    	model.setItens(createItensPedidoModel(pedido.getItens()));
    	model.setStatus(pedido.getStatus());
    	model.setValor(pedido.getValor());
    	
    	return model;
    }
    
	private List<ItemPedidoModel> createItensPedidoModel(List<ItemPedido> itens) {
		return itens.stream().map(i -> createItemPedidoModel(i)).collect(Collectors.toList());
	}
	
	private ItemPedidoModel createItemPedidoModel(ItemPedido itemPedido) {
		ItemPedidoModel model = new ItemPedidoModel();
		
		model.setPrecoTotal(itemPedido.getPrecoTotal());
		model.setProduto(createProdutoModel(itemPedido.getProduto()));
		model.setQuantidade(itemPedido.getQuantidade());
		
		return model;
	}
	
	private ProdutoModel createProdutoModel(Produto produto) {
		ProdutoModel model = new ProdutoModel();
		
		model.setCategoria(createCategoriaResumoModel(produto.getCategoria()));
		model.setDescricao(produto.getDescricao());
		model.setId(produto.getId());
		model.setImagem(produto.getImagem());
		model.setNome(produto.getNome());
		model.setPreco(produto.getPreco());
	
		return model;
	}
	
	private CategoriaResumoModel createCategoriaResumoModel(Categoria categoria) {
		CategoriaResumoModel model = new CategoriaResumoModel();
		
		model.setNome(categoria.getNome());
		
		return model;
	}
	
    private ClienteModel createClienteModel(Cliente cliente) {
    	ClienteModel model = new ClienteModel();
    	
    	model.setCpf(cliente.getCpf());
    	model.setEmail(cliente.getEmail());
    	model.setId(cliente.getId());
    	model.setNome(cliente.getNome());
    	
    	return model;
    }
    
	private List<ItemPedidoInput> createItensPedidoInput(List<ItemPedido> itens) {
		return itens.stream().map(i -> createItemPedidoInput(i)).collect(Collectors.toList());
	}
	
	private ItemPedidoInput createItemPedidoInput(ItemPedido itemPedido) {
		ItemPedidoInput input = new ItemPedidoInput();
		
		input.setProdutoId(itemPedido.getProduto().getId());
		input.setQuantidade(itemPedido.getQuantidade());
		
		return input;
	}
	
    private PedidoInput createPedidoInput(Pedido pedido) {
    	PedidoInput input = new PedidoInput();
    	
    	input.setClienteId(pedido.getCliente().getId());
    	input.setItens(createItensPedidoInput(pedido.getItens()));
    	
    	return input;
    }
    
    @Test
	public void checkout() {
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Pedido expectedPedido = createPedido(1L, cliente);
		PedidoInput pedidoInput = createPedidoInput(expectedPedido);
		PedidoModel expectedPedidoModel = createPedidoModel(expectedPedido);
		
		when(pedidoMapper.toDomainObject(pedidoInput)).thenReturn(expectedPedido);
		when(pedidoMapper.toModel(pedido)).thenReturn(expectedPedidoModel);
		when(useCase.checkout(pedido)).thenReturn(expectedPedido);
		
		assertEquals(controller.checkout(pedidoInput), expectedPedidoModel);
	}
}
