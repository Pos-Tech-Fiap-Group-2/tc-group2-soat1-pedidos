package com.techchallenge.pedidos.adapter.gateways.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.mapper.business.ItemPedidoBusinessMapper;
import com.techchallenge.pedidos.adapter.mapper.business.PedidoBusinessMapper;
import com.techchallenge.pedidos.adapter.mapper.business.ProdutoBusinessMapper;
import com.techchallenge.pedidos.adapter.mapper.db.ItemPedidoEntityMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;
import com.techchallenge.pedidos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pedidos.core.domain.exception.NegocioException;
import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;
import com.techchallenge.pedidos.drivers.db.entities.ClienteEntity;
import com.techchallenge.pedidos.drivers.db.entities.ItemPedidoEntity;
import com.techchallenge.pedidos.drivers.db.entities.PedidoEntity;
import com.techchallenge.pedidos.drivers.db.entities.ProdutoEntity;
import com.techchallenge.pedidos.drivers.db.repositories.ItemPedidoRepository;
import com.techchallenge.pedidos.drivers.db.repositories.PedidoRepository;
import com.techchallenge.pedidos.drivers.db.repositories.ProdutoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class ItemPedidoGatewayImplTest {

	@InjectMocks
	private ItemPedidoGatewayImpl gateway;
	
    @Mock
    private ItemPedidoRepository repository;
    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ItemPedidoEntityMapper mapper;
    @Mock
    private ItemPedidoBusinessMapper businessMapper;
    @Mock
    private PedidoBusinessMapper pedidoBusinessMapper;
    @Mock
    private ProdutoBusinessMapper produtoBusinessMapper;
    
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
    
	private CategoriaEntity createCategoriaEntity(Long id, String nome) {
		CategoriaEntity categoria = new CategoriaEntity();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
	}
	
	private Produto createProduto(ProdutoEntity entity) {
		Produto produto = new Produto();
		
		produto.setCategoria(createCategoria(entity.getCategoria()));
		produto.setDescricao(entity.getDescricao());
		produto.setId(entity.getId());
		produto.setImagem(entity.getImagem());
		produto.setNome(entity.getNome());
		produto.setPreco(entity.getPreco());
		
		return produto;
	}
	
	private Categoria createCategoria(CategoriaEntity entity) {
		Categoria categoria = new Categoria();
		
		categoria.setId(entity.getId());
		categoria.setNome(entity.getNome());
		
		return categoria;
	}
	
	private Cliente createCliente(ClienteEntity entity) {
		Cliente cliente = new Cliente();
		
		cliente.setCpf(entity.getCpf());
		cliente.setEmail(entity.getEmail());
		cliente.setId(entity.getId());
		cliente.setNome(entity.getNome());
		
		return cliente;
	}
	
	private ClienteEntity createClienteEntity(Long cpf, String email, Long id, String nome) {
		ClienteEntity entity = new ClienteEntity();
		
		entity.setCpf(cpf);
		entity.setEmail(email);
		entity.setId(id);
		entity.setNome(nome);
		
		return entity;
	}
    
	private ItemPedidoEntity createItemPedidoEntity(Long id, PedidoEntity pedido, BigDecimal precoTotal, ProdutoEntity produtoEntity, Integer quantidade) {
		ItemPedidoEntity itemPedido = new ItemPedidoEntity();
		
		itemPedido.setId(id);
		itemPedido.setPedido(pedido);
		itemPedido.setPrecoTotal(precoTotal);
		itemPedido.setProduto(produtoEntity);
		itemPedido.setQuantidade(quantidade);
		
		return itemPedido;
	}
	
	@SuppressWarnings("serial")
	private List<ItemPedidoEntity> createItemPedidoEntities(PedidoEntity pedidoEntity) {
		
		return new ArrayList<ItemPedidoEntity>() {{
			this.add(createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99")), 2));
			this.add(createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("20.99"), createProdutoEntity("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger duplo", new BigDecimal("20.99")), 1));
		}};
	}
	
	private ItemPedido createItemPedido(ItemPedidoEntity entity, Pedido pedido) {
		ItemPedido itemPedido = new ItemPedido();
		
		itemPedido.setId(entity.getId());
		itemPedido.setPedido(pedido);
		itemPedido.setPrecoTotal(entity.getPrecoTotal());
		itemPedido.setProduto(createProduto(entity.getProduto()));
		itemPedido.setQuantidade(entity.getQuantidade());
		
		return itemPedido;
	}
	
	private List<ItemPedido> createItemPedidos(List<ItemPedidoEntity> entities, Pedido pedido) {
		return entities.stream().map(e -> createItemPedido(e, pedido)).collect(Collectors.toList());
	}
	
    private Pedido createPedido(PedidoEntity entity) {
    	Pedido pedido = new Pedido();
    	
    	pedido.setCliente(createCliente(entity.getCliente()));
    	pedido.setDataCancelamento(entity.getDataCancelamento());
    	pedido.setDataFinalizacao(entity.getDataCancelamento());
    	pedido.setDataSolicitacao(entity.getDataSolicitacao());
    	pedido.setId(entity.getId());
    	pedido.setItens(createItemPedidos(entity.getItens(), pedido));
    	pedido.setPaymentId(entity.getPaymentId());
    	pedido.setStatus(entity.getStatus());
    	pedido.setValor(entity.getValor());
    	
    	return pedido;
    }
    
    private PedidoEntity createPedidoEntity(ClienteEntity cliente) {
    	PedidoEntity entity = new PedidoEntity();
    	
    	entity.setCliente(cliente);
    	entity.setDataSolicitacao(OffsetDateTime.now());
    	entity.setItens(createItemPedidoEntities(entity));
    	entity.setStatus(StatusPedido.RECEBIDO);
    	
    	return entity;
    }
    
    private ProdutoEntity createProdutoEntity(String descricao, Long id, String imagem, String nome, BigDecimal preco) {
    	ProdutoEntity entity = new ProdutoEntity();
    	
    	entity.setCategoria(createCategoriaEntity(1L, "Lanche"));
    	entity.setDescricao(descricao);
    	entity.setId(id);
    	entity.setImagem(imagem);
    	entity.setNome(nome);
    	entity.setPreco(preco);
    	
    	return entity;
    }
    
    @Test
    public void adicionarItemAoPedido() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	ItemPedidoEntity expectedEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	ItemPedido expected = createItemPedido(expectedEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.of(pedidoEntity);    	
    	Optional<ProdutoEntity> expectedProdutoEntity = Optional.of(produtoEntity);
    	
    	when(pedidoRepository.findByIdAndStatus(pedido.getId(), StatusPedido.RECEBIDO)).thenReturn(expectedPedidoEntity);
    	when(produtoRepository.findById(itemPedidoEntity.getProduto().getId())).thenReturn(expectedProdutoEntity);
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(List.of());
    	when(repository.save(itemPedidoEntity)).thenReturn(expectedEntity);
    	when(pedidoBusinessMapper.toModel(pedidoEntity)).thenReturn(pedido);
    	when(produtoBusinessMapper.toModel(produtoEntity)).thenReturn(produto);
    	when(mapper.toModel(itemPedido)).thenReturn(itemPedidoEntity);
    	when(businessMapper.toModel(itemPedidoEntity)).thenReturn(itemPedido);
    	
    	assertEquals(gateway.adicionarItemAoPedido(pedido.getId(), itemPedido), expected);
    }
    
    @Test
    public void atualizarItemAoPedido() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	List<ItemPedidoEntity> itemPedidoEntities = createItemPedidoEntities(pedidoEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.of(pedidoEntity);    	
    	Optional<ProdutoEntity> expectedProdutoEntity = Optional.of(produtoEntity);
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(itemPedidoEntities);
    	when(pedidoRepository.findById(pedido.getId())).thenReturn(expectedPedidoEntity);
    	when(produtoRepository.findById(produto.getId())).thenReturn(expectedProdutoEntity);
    	when(produtoBusinessMapper.toModel(produtoEntity)).thenReturn(produto);
    	when(pedidoBusinessMapper.toModel(pedidoEntity)).thenReturn(pedido);
    	
    	assertDoesNotThrow(() -> gateway.atualizarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void excluirItemAoPedido() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	List<ItemPedidoEntity> itemPedidoEntities = createItemPedidoEntities(pedidoEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.of(pedidoEntity);    	
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(itemPedidoEntities);
    	when(pedidoRepository.findById(pedido.getId())).thenReturn(expectedPedidoEntity);
    	when(pedidoBusinessMapper.toModel(pedidoEntity)).thenReturn(pedido);
    	
    	doNothing().when(repository).deleteById(itemPedido.getId());
    	doNothing().when(repository).flush();
    	
    	assertDoesNotThrow(() -> gateway.excluirItemAoPedido(pedido.getId(), produto.getId()));
    }
    
    @Test
    public void adicionarItemAoPedido_then_throws_Pedido_Nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.empty();
    	
    	when(pedidoRepository.findByIdAndStatus(pedido.getId(), StatusPedido.RECEBIDO)).thenReturn(expectedPedidoEntity);
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.adicionarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void adicionarItemAoPedido_then_throws_Produto_Nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.of(pedidoEntity);
    	Optional<ProdutoEntity> expectedProdutoEntity  = Optional.empty();
    	
    	when(pedidoRepository.findByIdAndStatus(pedido.getId(), StatusPedido.RECEBIDO)).thenReturn(expectedPedidoEntity);
    	when(produtoRepository.findById(itemPedidoEntity.getProduto().getId())).thenReturn(expectedProdutoEntity);
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.adicionarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void adicionarItemAoPedido_then_throws_Itens_ja_existentes() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	List<ItemPedidoEntity> itemPedidoEntities = createItemPedidoEntities(pedidoEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.of(pedidoEntity);    	
    	Optional<ProdutoEntity> expectedProdutoEntity = Optional.of(produtoEntity);
    	
    	when(pedidoRepository.findByIdAndStatus(pedido.getId(), StatusPedido.RECEBIDO)).thenReturn(expectedPedidoEntity);
    	when(produtoRepository.findById(itemPedidoEntity.getProduto().getId())).thenReturn(expectedProdutoEntity);
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(itemPedidoEntities);
    	
    	assertThrows(NegocioException.class, () -> gateway.adicionarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void atualizarItemAoPedido_then_throws_Item_do_pedido_nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(List.of());
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.atualizarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void atualizarItemAoPedido_then_throws_Pedido_Nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	List<ItemPedidoEntity> itemPedidoEntities = createItemPedidoEntities(pedidoEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.empty();    	
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(itemPedidoEntities);
    	when(pedidoRepository.findById(pedido.getId())).thenReturn(expectedPedidoEntity);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.atualizarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void atualizarItemAoPedido_then_throws_Produto_Nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	List<ItemPedidoEntity> itemPedidoEntities = createItemPedidoEntities(pedidoEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.of(pedidoEntity);    	
    	Optional<ProdutoEntity> expectedProdutoEntity = Optional.empty();
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(itemPedidoEntities);
    	when(pedidoRepository.findById(pedido.getId())).thenReturn(expectedPedidoEntity);
    	when(produtoRepository.findById(produto.getId())).thenReturn(expectedProdutoEntity);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.atualizarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void atualizarItemAoPedido_then_throws_Item_Pedido_Nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	List<ItemPedidoEntity> itemPedidoEntities = createItemPedidoEntities(pedidoEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	// Simular com ID diferente para não realizar a atualização.
    	pedido.getItens().forEach(item -> item.setId(2L));
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.of(pedidoEntity);    	
    	Optional<ProdutoEntity> expectedProdutoEntity = Optional.of(produtoEntity);
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(itemPedidoEntities);
    	when(pedidoRepository.findById(pedido.getId())).thenReturn(expectedPedidoEntity);
    	when(produtoRepository.findById(produto.getId())).thenReturn(expectedProdutoEntity);
    	when(produtoBusinessMapper.toModel(produtoEntity)).thenReturn(produto);
    	when(pedidoBusinessMapper.toModel(pedidoEntity)).thenReturn(pedido);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.atualizarItemAoPedido(pedido.getId(), itemPedido));
    }
    
    @Test
    public void excluirItemAoPedido_then_throws_Item_Pedido_Nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(List.of());
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.excluirItemAoPedido(pedido.getId(), produto.getId()));
    }
    
    @Test
    public void excluirItemAoPedido_then_throws_Pedido_Nao_encontrado() {
    	ProdutoEntity produtoEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ClienteEntity clienteEntity = createClienteEntity(12345678901L, "cliente.teste@teste.com.br", 1L, "Cliente Teste");
    	PedidoEntity pedidoEntity = createPedidoEntity(clienteEntity);
    	ItemPedidoEntity itemPedidoEntity = createItemPedidoEntity(1L, pedidoEntity, new BigDecimal("25.99"), produtoEntity, 1);
    	
    	List<ItemPedidoEntity> itemPedidoEntities = createItemPedidoEntities(pedidoEntity);
    	
    	Pedido pedido = createPedido(pedidoEntity);
    	Produto produto = createProduto(produtoEntity);
    	ItemPedido itemPedido = createItemPedido(itemPedidoEntity, pedido);
    	
    	Optional<PedidoEntity> expectedPedidoEntity  = Optional.empty();    	
    	
    	when(repository.findByPedidoAndProduto(pedido.getId(), produto.getId())).thenReturn(itemPedidoEntities);
    	when(pedidoRepository.findById(pedido.getId())).thenReturn(expectedPedidoEntity);
    	
    	doNothing().when(repository).deleteById(itemPedido.getId());
    	doNothing().when(repository).flush();
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.excluirItemAoPedido(pedido.getId(), produto.getId()));
    }
}
