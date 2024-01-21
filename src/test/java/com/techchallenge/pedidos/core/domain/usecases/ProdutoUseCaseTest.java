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

import com.techchallenge.pedidos.adapter.gateways.ProdutoGateway;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Produto;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProdutoUseCaseTest {

	@InjectMocks
	private ProdutoUseCase useCase;
	
	@Mock
    private ProdutoGateway gateway;

	@Mock
    private CategoriaUseCase categoriaUseCase;
	
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
	
	@Test
    public void salvar() {
    	Produto entity = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto expected = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	
    	Categoria categoriaExpected = createCategoria(1L, "Lanche");
    	Long categoriaId = entity.getCategoria().getId();
    	
    	when(categoriaUseCase.buscarPorId(categoriaId)).thenReturn(categoriaExpected);
    	when(gateway.salvar(entity)).thenReturn(expected);
    	
    	assertEquals(this.useCase.salvar(entity), expected);
    }

	@Test
    public void editar() {
    	Long id = 1L;
    	Produto expected = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	
    	when(gateway.editar(id)).thenReturn(expected);
    	assertEquals(useCase.editar(id), expected);
    }

	@Test
    public void excluir() {
		Long id = 1L;
		
		doNothing().when(gateway).excluir(id);
    	assertDoesNotThrow(() -> useCase.excluir(id));
    }
    
	@Test
    public void buscarTodos() {
    	List<Produto> expected = new ArrayList<>();
    	
    	expected.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	when(gateway.buscarTodos()).thenReturn(expected);
    	assertTrue(useCase.buscarTodos().size() > 0);
    }

    @Test
    public void buscarPorProdutoCategoria() {
    	List<Produto> expected = new ArrayList<>();
    	
    	expected.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	when(gateway.buscarPorCategoria("Lanche")).thenReturn(expected);
    	assertTrue(useCase.buscarPorCategoria("Lanche").size() > 0);
    	
    }
    
    @Test
    public void buscarPorCategoria() {
    	Long categoriaId = 1L;
    	
    	List<Produto> expected = new ArrayList<>();
    	
    	expected.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	expected.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));

    	when(gateway.buscarPorCategoria(categoriaId)).thenReturn(expected);
    	assertTrue(useCase.buscarPorCategoria(categoriaId).size() > 0);
    }
    
    @Test
    public void buscarPorId() {
    	Produto entity = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	Produto expected = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	
    	when(gateway.buscarPorId(entity.getId())).thenReturn(expected);
    	assertEquals(useCase.buscarPorId(entity.getId()), expected);
    }
    
    @Test
    public void atualizar() {
    	Produto entity = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
    	
    	Categoria categoriaExpected = createCategoria(1L, "Lanche");
    	Long categoriaId = entity.getCategoria().getId();
    	
        when(categoriaUseCase.buscarPorId(categoriaId)).thenReturn(categoriaExpected);
        doNothing().when(gateway).atualizar(entity.getId(), entity);
        
        assertDoesNotThrow(() -> useCase.atualizar(categoriaId, entity));
        
    }
}
