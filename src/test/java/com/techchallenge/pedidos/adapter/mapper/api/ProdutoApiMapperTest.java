package com.techchallenge.pedidos.adapter.mapper.api;

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
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.driver.model.CategoriaResumoModel;
import com.techchallenge.pedidos.adapter.driver.model.ProdutoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ProdutoInput;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Produto;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProdutoApiMapperTest {

	@InjectMocks
	private ProdutoApiMapper api;
	
	@Mock
	private ModelMapper mapper;
	
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
	public void toDomainObject() {
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", null, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
		Produto expected = createProduto("Cheeseburger com duplo de carne e queijo", null, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
		ProdutoInput input = createProdutoInput(produto);
		
		when(mapper.map(input, Produto.class)).thenReturn(produto);
		assertEquals(api.toDomainObject(input), expected);
    }

	@Test
    public void toModel() {
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
		ProdutoModel expected = createProdutoModel(produto);
		
		when(mapper.map(produto, ProdutoModel.class)).thenReturn(expected);
        assertEquals(api.toModel(produto), expected);
    }

	@Test
    public void toCollectionModel() {
    	List<Produto> produtos = new ArrayList<>();
    	Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	ProdutoModel model = createProdutoModel(produto);
    	
    	produtos.add(produto);
    	
    	when(mapper.map(produto, ProdutoModel.class)).thenReturn(model);
    	assertTrue(api.toCollectionModel(produtos).size() > 0);
    }

	@Test
    public void copyToDomainObject() {
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
		ProdutoInput input = createProdutoInput(produto);
		
		doNothing().when(mapper).map(input, produto);
		assertDoesNotThrow(() -> api.copyToDomainObject(input, produto));
    }
}
