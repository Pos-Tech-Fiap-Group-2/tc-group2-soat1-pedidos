package com.techchallenge.pedidos.adapter.controllers;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
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
import com.techchallenge.pedidos.adapter.driver.model.ProdutoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ProdutoInput;
import com.techchallenge.pedidos.adapter.mapper.api.ProdutoApiMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.usecases.ProdutoUseCase;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProdutoControllerTest {

	@InjectMocks
	private ProdutoController controller;
	
	@Mock
	private ProdutoUseCase useCase;
	@Mock
	private ProdutoApiMapper mapper;
	
    @BeforeEach
    private void setup() throws NoSuchFieldException, IllegalAccessException {
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
	
	private List<ProdutoModel> createProdutoModels(List<Produto> produtos) {
		return produtos.stream().map(p -> createProdutoModel(p)).collect(Collectors.toList());
	}
	
	private CategoriaResumoModel createCategoriaResumoModel(Categoria categoria) {
		CategoriaResumoModel model = new CategoriaResumoModel();
		
		model.setNome(categoria.getNome());
		
		return model;
	}
	
	private ProdutoInput createProdutoInput(Produto produto) {
		ProdutoInput input = new ProdutoInput();
		
		input.setCategoriaId(produto.getCategoria().getId());
		input.setDescricao(produto.getDescricao());
		input.setImagem(produto.getImagem());
		input.setNome(produto.getNome());
		input.setPreco(produto.getPreco());
		
		return input;
	}
    
    @Test
    public void listar() {
    	List<Produto> expected = new ArrayList<>();
    	
    	expected.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<ProdutoModel> expectedProdutoModels = createProdutoModels(expected);
    	
    	when(useCase.buscarTodos()).thenReturn(expected);
    	when(mapper.toCollectionModel(expected)).thenReturn(expectedProdutoModels);
    	
    	assertTrue(controller.listar().size() > 0);
    }

    @Test
    public void listarPorCategoria_por_nome() {
    	String nome = "Lanche";
    	List<Produto> expected = new ArrayList<>();
    	
    	expected.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<ProdutoModel> expectedProdutoModels = createProdutoModels(expected);
    	
    	when(useCase.buscarPorCategoria(nome)).thenReturn(expected);
    	when(mapper.toCollectionModel(expected)).thenReturn(expectedProdutoModels);
    	
    	assertTrue(controller.listarPorCategoria(nome).size() > 0);
    }
    
    @Test
    public void listarPorCategoria() {
    	Long id = 1L;
    	List<Produto> expected = new ArrayList<>();
    	
    	expected.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<ProdutoModel> expectedProdutoModels = createProdutoModels(expected);
    	
    	when(useCase.buscarPorCategoria(id)).thenReturn(expected);
    	when(mapper.toCollectionModel(expected)).thenReturn(expectedProdutoModels);
    	
    	assertTrue(controller.listarPorCategoria(id).size() > 0);
    }
    
    @Test
	public void adicionar() {
    	Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	Produto expectedProduto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	ProdutoModel expectedProdutoModel = createProdutoModel(produto);
    	ProdutoInput produtoInput = createProdutoInput(produto);
    	
    	when(mapper.toDomainObject(produtoInput)).thenReturn(expectedProduto);
    	when(useCase.salvar(produto)).thenReturn(expectedProduto);
    	when(mapper.toModel(produto)).thenReturn(expectedProdutoModel);
    	
    	assertEquals(controller.adicionar(produtoInput), expectedProdutoModel);
    	
	}
	
    @Test
	public void remover() {
    	Long id = 1L;
    	
    	doNothing().when(useCase).excluir(id);
		assertDoesNotThrow(() -> controller.remover(id));
	}
	
    @Test
	public void atualizar() {
    	Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	Produto expectedProduto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	ProdutoInput produtoInput = createProdutoInput(produto);
    	ProdutoInput expectedProdutoInput = createProdutoInput(produto);
    	
    	when(mapper.toDomainObject(expectedProdutoInput)).thenReturn(expectedProduto);
    	doNothing().when(useCase).atualizar(produto.getId(), expectedProduto);

    	assertDoesNotThrow(() -> controller.atualizar(produto.getId(), produtoInput));
	}
}
