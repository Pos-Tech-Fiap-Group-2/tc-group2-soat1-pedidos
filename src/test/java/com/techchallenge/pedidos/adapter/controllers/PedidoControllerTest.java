package com.techchallenge.pedidos.adapter.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
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
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.driver.model.CategoriaResumoModel;
import com.techchallenge.pedidos.adapter.driver.model.ClienteModel;
import com.techchallenge.pedidos.adapter.driver.model.ItemPedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.PedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.ProdutoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ItemPedidoInput;
import com.techchallenge.pedidos.adapter.mapper.api.ItemPedidoApiMapper;
import com.techchallenge.pedidos.adapter.mapper.api.PedidoApiMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;
import com.techchallenge.pedidos.core.domain.usecases.ItemPedidoUseCase;
import com.techchallenge.pedidos.core.domain.usecases.PedidoUseCase;
import com.techchallenge.pedidos.core.modelmapper.ModelMapperConfig;

@RunWith(SpringJUnit4ClassRunner.class)
public class PedidoControllerTest {
	
	@InjectMocks
	private PedidoController controller;
	
    @Mock
    private PedidoUseCase useCase;
    @Mock
    private ItemPedidoUseCase itemPedidoUseCase;
    @Spy
    private PedidoApiMapper pedidoMapper = new PedidoApiMapper();
    @Mock
    private ItemPedidoApiMapper itemPedidoMapper;
    
    private ModelMapperConfig modelMapperConfig = new ModelMapperConfig();

    @BeforeEach
    private void setup() throws NoSuchFieldException, IllegalAccessException {
    	MockitoAnnotations.initMocks(this);
    	confgureSpyMapper();
    	
    }

	private void confgureSpyMapper() throws NoSuchFieldException, IllegalAccessException {
    	Field fieldMapper = PedidoApiMapper.class.getDeclaredField("mapper");
    	
    	fieldMapper.setAccessible(Boolean.TRUE);
    	fieldMapper.set(pedidoMapper, modelMapperConfig.modelMapper());
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
    
	private ItemPedidoInput createItemPedidoInput(ItemPedido itemPedido) {
		ItemPedidoInput input = new ItemPedidoInput();
		
		input.setProdutoId(itemPedido.getProduto().getId());
		input.setQuantidade(itemPedido.getQuantidade());
		
		return input;
	}
	
    @Test
    public void buscarPedidosPorId() {
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Pedido expectedPedido = createPedido(1L, cliente);
		PedidoModel expectedModel = createPedidoModel(pedido);
		
		when(useCase.buscarPedidoPorId(pedido.getId())).thenReturn(expectedPedido);
		when(pedidoMapper.toModel(pedido)).thenReturn(expectedModel);
		
		assertEquals(controller.buscarPedidosPorId(pedido.getId()), expectedModel);
    }

    @Test
    public void listarPedidos_por_ordem_status() {
    	List<Pedido> pedidos = new ArrayList<>();
    	List<Pedido> expectedPedidos = new ArrayList<>();
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		
		pedidos.add(createPedido(1L, cliente));
		pedidos.add(createPedido(2L, cliente));
		
		expectedPedidos.add(createPedido(1L, cliente));
		expectedPedidos.add(createPedido(2L, cliente));
		
		when(useCase.buscarPedidosPorStatus(StatusPedido.RECEBIDO)).thenReturn(expectedPedidos);
		assertTrue(controller.listarPedidos(StatusPedido.RECEBIDO).stream()
				.filter(p -> p.getStatus() == StatusPedido.RECEBIDO)
				.count() == expectedPedidos.size());
    }
    
    @Test
    public void listarPedidos() {
    	List<Pedido> expectedPedidos = new ArrayList<>();
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		
		expectedPedidos.add(createPedido(1L, cliente));
		expectedPedidos.add(createPedido(2L, cliente));
		
    	when(useCase.buscarPedidos()).thenReturn(expectedPedidos);
		assertTrue(controller.listarPedidos().stream()
				.filter(p -> p.getStatus() == StatusPedido.RECEBIDO)
				.count() == expectedPedidos.size());
    }

    @Test
    public void atualizarStatusDoPedido() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Pedido expectedPedido = createPedido(1L, cliente);
		
		when(useCase.buscarPedidoPorId(pedido.getId())).thenReturn(expectedPedido);
		doNothing().when(useCase).atualizarStatusDoPedido(pedido, StatusPedido.PREPARACAO);
		
		assertDoesNotThrow(() -> controller.atualizarStatusDoPedido(pedido.getId(), StatusPedido.PREPARACAO.name()));
    }
    
    @Test
	public void adicionarProduto() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
    	ItemPedido expectedItemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
    	ItemPedidoInput itemPedidoInput = createItemPedidoInput(itemPedido);
    	ItemPedidoModel expectedItemPedidoModel = createItemPedidoModel(expectedItemPedido);
    	
    	when(itemPedidoMapper.toDomainObject(itemPedidoInput)).thenReturn(expectedItemPedido);
    	when(itemPedidoUseCase.adicionarItemAoPedido(pedido.getId(), itemPedido)).thenReturn(expectedItemPedido);
    	when(itemPedidoMapper.toModel(itemPedido)).thenReturn(expectedItemPedidoModel);
    	
    	assertEquals(controller.adicionarProduto(pedido.getId(), itemPedidoInput), expectedItemPedidoModel);
	}
	
    @Test
	public void atualizarProduto() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
		ItemPedido expectedItemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
		ItemPedidoInput itemPedidoInput = createItemPedidoInput(itemPedido);
		
    	when(itemPedidoMapper.toDomainObject(itemPedidoInput)).thenReturn(expectedItemPedido);
    	doNothing().when(itemPedidoUseCase).atualizarItemAoPedido(pedido.getId(), itemPedido);
    	
    	assertDoesNotThrow(() -> controller.atualizarProduto(pedido.getId(), itemPedidoInput));
	}
	
    @Test
	public void remover() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
		ItemPedidoInput itemPedidoInput = createItemPedidoInput(itemPedido);
		
    	doNothing().when(itemPedidoUseCase).excluirItemAoPedido(pedido.getId(), produto.getId());
		assertDoesNotThrow(() -> controller.remover(pedido.getId(), itemPedidoInput));
	}
}
