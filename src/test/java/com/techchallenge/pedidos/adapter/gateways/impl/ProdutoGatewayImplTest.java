package com.techchallenge.pedidos.adapter.gateways.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.mapper.business.ProdutoBusinessMapper;
import com.techchallenge.pedidos.adapter.mapper.db.ProdutoEntityMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.exception.EntidadeEmUsoException;
import com.techchallenge.pedidos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;
import com.techchallenge.pedidos.drivers.db.entities.ProdutoEntity;
import com.techchallenge.pedidos.drivers.db.repositories.ProdutoRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProdutoGatewayImplTest {

	@InjectMocks
	private ProdutoGatewayImpl gateway;
	
    @Mock
    private ProdutoRepository repository;
    @Mock
    private ProdutoEntityMapper mapper;
    @Mock
    private ProdutoBusinessMapper businessMapper;
    
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
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
	
	private CategoriaEntity createCategoriaEntity(Long id, String nome) {
		CategoriaEntity categoria = new CategoriaEntity();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
	}
	
	private Categoria createCategoria(CategoriaEntity entity) {
		Categoria categoria = new Categoria();
		
		categoria.setId(entity.getId());
		categoria.setNome(entity.getNome());
		
		return categoria;
	}
    
    @Test
    public void salvar() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	ProdutoEntity expectedEntity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	
    	Produto produto = createProduto(entity);
    	Produto expectedProduto = createProduto(entity);
    	
    	when(repository.save(entity)).thenReturn(expectedEntity);
    	when(mapper.toModel(produto)).thenReturn(entity);
    	when(businessMapper.toModel(entity)).thenReturn(expectedProduto);
    	
    	assertEquals(gateway.salvar(produto), expectedProduto);
    }

    @Test
    public void editar() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	Produto expected = createProduto(entity);
    	
    	Optional<ProdutoEntity> optionalEntity = Optional.of(entity);
    	
    	when(repository.findById(entity.getId())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(produto);
    	
    	assertEquals(gateway.editar(produto.getId()), expected);
    }

    @Test
    public void excluir() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	
    	doNothing().when(repository).deleteById(produto.getId());
    	doNothing().when(repository).flush();
    	
    	assertDoesNotThrow(() -> gateway.excluir(produto.getId()));
    }
    
    @Test
    public void buscarTodos() {
    	List<ProdutoEntity> expected = new ArrayList<>();
    	
    	expected.add(createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProdutoEntity("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<Produto> produtos = expected.stream().map(e -> createProduto(e)).collect(Collectors.toList());
    	
    	when(repository.findAll()).thenReturn(expected);
    	when(businessMapper.toCollectionModel(expected)).thenReturn(produtos);
    	
    	assertTrue(gateway.buscarTodos().size() > 0);
    }

    @Test
    public void buscarPorCategoria_Descricao() {
    	List<ProdutoEntity> expected = new ArrayList<>();
    	
    	expected.add(createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProdutoEntity("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<Produto> produtos = expected.stream().map(e -> createProduto(e)).collect(Collectors.toList());
    	
    	when(repository.findByCategoriaNome("Lanche")).thenReturn(expected);
    	when(businessMapper.toCollectionModel(expected)).thenReturn(produtos);
    	
    	assertTrue(gateway.buscarPorCategoria("Lanche").size() > 0);
    }
    
    @Test
    public void buscarPorCategoria_Id() {
    	List<ProdutoEntity> expected = new ArrayList<>();
    	
    	expected.add(createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProdutoEntity("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<Produto> produtos = expected.stream().map(e -> createProduto(e)).collect(Collectors.toList());
    	
    	when(repository.findByCategoriaId(1L)).thenReturn(expected);
    	when(businessMapper.toCollectionModel(expected)).thenReturn(produtos);
    	
    	assertTrue(gateway.buscarPorCategoria(1L).size() > 0);
    }
    
    @Test
    public void atualizar() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	
    	Optional<ProdutoEntity> optionalEntity = Optional.of(entity);
    	
    	when(repository.findById(produto.getId())).thenReturn(optionalEntity);
    	assertDoesNotThrow(() -> this.gateway.atualizar(produto.getId(), produto));
    }

    @Test
	public void buscarPorId() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	Produto expected = createProduto(entity);
    	
    	Optional<ProdutoEntity> optionalEntity = Optional.of(entity);
    	
    	when(repository.findById(produto.getId())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(expected);
    	
    	assertEquals(gateway.buscarPorId(produto.getId()), expected);
	}
    
    @Test
    public void editar_then_throws_Entidade_Nao_Encontrada() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	
    	Optional<ProdutoEntity> optionalEntity = Optional.empty();
    	
    	when(repository.findById(entity.getId())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(produto);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.editar(produto.getId()));
    }
    
    @Test
    public void excluir_then_throws_Entidade_Em_Uso() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	
    	doThrow(DataIntegrityViolationException.class).when(repository).deleteById(produto.getId());
    	assertThrows(EntidadeEmUsoException.class, () -> gateway.excluir(produto.getId()));
    }
    
    @Test
    public void atualizar_then_throws_Entidade_Nao_encontrada() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	
    	Optional<ProdutoEntity> optionalEntity = Optional.empty();
    	
    	when(repository.findById(produto.getId())).thenReturn(optionalEntity);
    	assertThrows(EntidadeNaoEncontradaException.class, () -> this.gateway.atualizar(produto.getId(), produto));
    }
    
    @Test
	public void buscarPorId_then_throws_Entidade_Nao_encontrada() {
    	ProdutoEntity entity = createProdutoEntity("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto produto = createProduto(entity);
    	Produto expected = createProduto(entity);
    	
    	Optional<ProdutoEntity> optionalEntity = Optional.empty();
    	
    	when(repository.findById(produto.getId())).thenReturn(optionalEntity);
    	when(businessMapper.toModel(entity)).thenReturn(expected);
    	
    	assertThrows(EntidadeNaoEncontradaException.class, () -> gateway.buscarPorId(produto.getId()));
	}
}
