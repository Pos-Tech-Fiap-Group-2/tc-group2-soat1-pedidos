package com.techchallenge.pedidos.adapter.gateways.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.mapper.business.PedidoBusinessMapper;
import com.techchallenge.pedidos.adapter.mapper.db.PedidoEntityMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;
import com.techchallenge.pedidos.core.domain.exception.EntidadeEmUsoException;
import com.techchallenge.pedidos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;
import com.techchallenge.pedidos.drivers.db.entities.ClienteEntity;
import com.techchallenge.pedidos.drivers.db.entities.ItemPedidoEntity;
import com.techchallenge.pedidos.drivers.db.entities.PedidoEntity;
import com.techchallenge.pedidos.drivers.db.entities.ProdutoEntity;
import com.techchallenge.pedidos.drivers.db.repositories.PedidoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class PedidoGatewayImplTest {

	@InjectMocks
	private PedidoGatewayImpl gateway;
	
    @Mock
    private PedidoRepository repository;
    @Mock
    private PedidoEntityMapper mapper;
    @Mock
    private PedidoBusinessMapper businessMapper;
    
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
    
	private ClienteEntity createClienteEntity(Long id, Long cpf, String email, String nome) {
		ClienteEntity entity = new ClienteEntity();
		
		entity.setCpf(cpf);
		entity.setEmail(email);
		entity.setId(id);
		entity.setNome(nome);
		
		return entity;
	}
	
	private CategoriaEntity createCategoriaEntity(Long id, String nome) {
		CategoriaEntity entity = new CategoriaEntity();
		
    	entity.setId(id);
    	entity.setNome(nome);
    	
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
	
	private ItemPedidoEntity createItemPedidoEntity(Long id, PedidoEntity pedido, BigDecimal precoTotal, ProdutoEntity produto, Integer quantidade) {
		ItemPedidoEntity entity = new ItemPedidoEntity();
		
		entity.setId(id);
		entity.setPedido(pedido);
		entity.setPrecoTotal(precoTotal);
		entity.setProduto(produto);
		entity.setQuantidade(quantidade);
		
		return entity;
	}
	
    private PedidoEntity createPedidoEntity(Long id, ClienteEntity cliente, StatusPedido status) {
    	PedidoEntity entity = new PedidoEntity();
    	
    	entity.setCliente(cliente);
    	entity.setId(id);
    	entity.setItens(createItemPedidoEntities(entity));
    	entity.setStatus(status);
    	entity.setDataSolicitacao(OffsetDateTime.now());
    	
    	return entity;
    }
	
	@SuppressWarnings("serial")
	private List<ItemPedidoEntity> createItemPedidoEntities(PedidoEntity pedido) {
		
		return new ArrayList<ItemPedidoEntity>() {{
			this.add(createItemPedidoEntity(1L, pedido, new BigDecimal("25.99"), createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99")), 2));
			this.add(createItemPedidoEntity(1L, pedido, new BigDecimal("20.99"), createProdutoEntity("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger duplo", new BigDecimal("20.99")), 1));
		}};
	}
	
	private List<ItemPedido> createItemPedidos(List<ItemPedidoEntity> entities) {
		return entities.stream().map(e -> createItemPedido(e)).collect(Collectors.toList());
	}
	
	private ItemPedido createItemPedido(ItemPedidoEntity entity) {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setId(entity.getId());
		itemPedido.setPrecoTotal(itemPedido.getPrecoTotal());
		itemPedido.setProduto(createProduto(entity.getProduto()));
		
		return itemPedido;
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
	
    private Pedido createPedido(PedidoEntity entity) {
    	Pedido pedido = new Pedido();
    	
    	pedido.setCliente(createCliente(entity.getCliente()));
    	pedido.setId(entity.getId());
    	pedido.setItens(createItemPedidos(entity.getItens()));
    	pedido.setStatus(entity.getStatus());
    	pedido.setDataCancelamento(entity.getDataCancelamento());
    	pedido.setDataFinalizacao(entity.getDataFinalizacao());
    	pedido.setDataSolicitacao(entity.getDataSolicitacao());
    	pedido.setPaymentId(entity.getPaymentId());
    	pedido.setValor(entity.getValor());
    	
    	return pedido;
    }
    
    @Test
    public void buscarPedidos() {
    	List<PedidoEntity> entities = new ArrayList<>();
    	
    	entities.add(createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO));
    	entities.add(createPedidoEntity(2L, createClienteEntity(2L, 12345678902L, "teste2@teste.com.br", "Teste 2"), StatusPedido.RECEBIDO));
    	
    	List<Pedido> pedidos = entities.stream().map(e -> createPedido(e)).collect(Collectors.toList());
    	
    	when(repository.findAll()).thenReturn(entities);
    	when(businessMapper.toCollectionModel(entities)).thenReturn(pedidos);
    	
    	assertTrue(gateway.buscarPedidos().size() > 0);
    }

    @Test
    public void buscarPedidoPorId() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	Pedido pedido = createPedido(entity);
    	Pedido expected = createPedido(entity);
    	
    	Optional<PedidoEntity> optionalEntity = Optional.of(entity);
    	
    	when(repository.findById(entity.getId())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(expected);
    	
    	assertEquals(gateway.buscarPedidoPorId(pedido.getId()), expected);
    }

    @Test
    public void buscarPedidosPorStatus() {
    	List<PedidoEntity> entities = new ArrayList<>();
    	
    	entities.add(createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO));
    	entities.add(createPedidoEntity(2L, createClienteEntity(2L, 12345678902L, "teste2@teste.com.br", "Teste 2"), StatusPedido.RECEBIDO));
    	
    	List<Pedido> pedidos = entities.stream().map(e -> createPedido(e)).collect(Collectors.toList());
    	
    	when(repository.findByStatus(StatusPedido.RECEBIDO)).thenReturn(entities);
    	when(businessMapper.toCollectionModel(entities)).thenReturn(pedidos);
    	
    	assertTrue(gateway.buscarPedidosPorStatus(StatusPedido.RECEBIDO).size() > 0);
    }
    
    @Test
    public void buscarPedidoPorIdEStatus() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	Pedido pedido = createPedido(entity);
    	Pedido expected = createPedido(entity);
    	
    	Optional<PedidoEntity> optionalEntity = Optional.of(entity);
    	
    	when(repository.findByIdAndStatus(entity.getId(), entity.getStatus())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(expected);
    	
    	assertEquals(gateway.buscarPedidoPorIdEStatus(pedido.getId(), pedido.getStatus()), expected);
    }

    @Test
    public void atualizarStatusDoPedido() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	PedidoEntity expectedEntity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	Pedido pedido = createPedido(entity);
    	
    	Optional<PedidoEntity> optionalEntity = Optional.of(entity);
    	
    	when(repository.findById(entity.getId())).thenReturn(optionalEntity);
    	when(repository.save(entity)).thenReturn(expectedEntity);
    	
    	assertDoesNotThrow(() -> gateway.atualizarStatusDoPedido(pedido, StatusPedido.PREPARACAO));
    }
    
    @Test
    public void excluir() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	
    	doNothing().when(repository).deleteById(entity.getId());
   		doNothing().when(repository).flush();
   		
   		assertDoesNotThrow(() -> gateway.excluir(entity.getId()));
    }

    @Test
	public void gravar() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	PedidoEntity expectedEntity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	Pedido pedido = createPedido(entity);
    	Pedido expectedPedido = createPedido(expectedEntity);
    	
    	when(mapper.toModel(pedido)).thenReturn(entity);
    	when(repository.save(entity)).thenReturn(expectedEntity);
    	when(businessMapper.toModel(entity)).thenReturn(expectedPedido);
    	
    	assertEquals(gateway.gravar(pedido), expectedPedido);
    	
	}
    
    @Test
    public void buscarPedidoPorId_then_throws_Entidade_nao_Encontrada() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	Pedido pedido = createPedido(entity);
    	Pedido expected = createPedido(entity);
    	
    	Optional<PedidoEntity> optionalEntity = Optional.empty();
    	
    	when(repository.findById(entity.getId())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(expected);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.buscarPedidoPorId(pedido.getId()));
    }
    
    @Test
    public void buscarPedidoPorIdEStatus_then_throws_Entidade_Nao_encontrada() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	Pedido pedido = createPedido(entity);
    	Pedido expected = createPedido(entity);
    	
    	Optional<PedidoEntity> optionalEntity = Optional.empty();
    	
    	when(repository.findByIdAndStatus(entity.getId(), entity.getStatus())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(expected);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.buscarPedidoPorIdEStatus(pedido.getId(), pedido.getStatus()));
    }
    
    @Test
    public void atualizarStatusDoPedido_then_throws_Entidade_Nao_encontrada() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	PedidoEntity expectedEntity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	Pedido pedido = createPedido(entity);
    	
    	Optional<PedidoEntity> optionalEntity = Optional.empty();
    	
    	when(repository.findById(entity.getId())).thenReturn(optionalEntity);
    	when(repository.save(entity)).thenReturn(expectedEntity);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.atualizarStatusDoPedido(pedido, StatusPedido.PREPARACAO));
    }
    
    @Test
    public void excluir_then_throws_Entidade_Em_Uso() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	
    	doThrow(DataIntegrityViolationException.class).when(repository).deleteById(entity.getId());
    	assertThrows(EntidadeEmUsoException.class, () -> gateway.excluir(entity.getId()));
    }
    
    @Test
    public void excluir_then_throws_Entidade_nao_encontrada() {
    	PedidoEntity entity = createPedidoEntity(1L, createClienteEntity(1L, 12345678901L, "teste1@teste.com.br", "Teste 1"), StatusPedido.RECEBIDO);
    	
    	doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(entity.getId());
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.excluir(entity.getId()));
    }
}
