package com.techchallenge.pedidos.adapter.mapper.business;

import static org.junit.Assert.assertTrue;
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
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;
import com.techchallenge.pedidos.drivers.db.entities.ProdutoEntity;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProdutoBusinessMapperTest {

	@InjectMocks
	private ProdutoBusinessMapper api;
	
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
	
	private ProdutoEntity createProdutoEntity(Produto produto) {
		ProdutoEntity entity = new ProdutoEntity();
		
		entity.setCategoria(createCategoriaEntity(produto.getCategoria()));
		entity.setDescricao(produto.getDescricao());
		entity.setId(produto.getId());
		entity.setImagem(produto.getImagem());
		entity.setNome(produto.getNome());
		entity.setPreco(produto.getPreco());
		
		return entity;
	}
	
	private CategoriaEntity createCategoriaEntity(Categoria categoria) {
		CategoriaEntity entity = new CategoriaEntity();
		
		entity.setId(categoria.getId());
		entity.setNome(categoria.getNome());
		
		return entity;
	}
    
	@Test
    public void toModel() {
    	Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	ProdutoEntity entity = createProdutoEntity(produto);
    	
    	when(mapper.map(entity, Produto.class)).thenReturn(produto);
    	assertEquals(api.toModel(entity), produto);
    }

	@Test
    public void toCollectionModel() {
    	List<ProdutoEntity> entities = new ArrayList<>();
    	Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	ProdutoEntity entity = createProdutoEntity(produto);
    	
    	entities.add(entity);
    	
    	when(mapper.map(entity, Produto.class)).thenReturn(produto);
    	assertTrue(api.toCollectionModel(entities).size() > 0);
    }
}
