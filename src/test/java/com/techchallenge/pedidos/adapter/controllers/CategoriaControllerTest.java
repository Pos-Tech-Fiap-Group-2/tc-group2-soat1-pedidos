package com.techchallenge.pedidos.adapter.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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

import com.techchallenge.pedidos.adapter.driver.model.CategoriaModel;
import com.techchallenge.pedidos.adapter.mapper.api.CategoriaApiMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.usecases.CategoriaUseCase;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoriaControllerTest {

	@InjectMocks
	private CategoriaController controller;
	
	@Mock
	private CategoriaUseCase useCase;
    @Mock
    private CategoriaApiMapper mapper;
    
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("serial")
	private List<Categoria> createCategorias() {
    	
    	return new ArrayList<Categoria>() {{
    		this.add(createCategoria(1L, "Lanche"));
    		this.add(createCategoria(2L, "Bebida"));
    		this.add(createCategoria(3L, "Acompanhamento"));
    		this.add(createCategoria(4L, "Sobremesa"));
    	}};
    }

	private Categoria createCategoria(Long id, String nome) {
		Categoria categoria = new Categoria();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
	}
	
	private CategoriaModel createCategoriaModel(Categoria categoria) {
		CategoriaModel model = new CategoriaModel();
		
    	model.setId(categoria.getId());
    	model.setNome(categoria.getNome());
    	
    	return model;
	}
	
	private List<CategoriaModel> createCategoriaModels(List<Categoria> categorias) {
		return categorias.stream().map(c -> createCategoriaModel(c)).collect(Collectors.toList());
	}
    
    @Test
    public void listar() {
        List<Categoria> categorias = createCategorias();
        List<CategoriaModel> expected = createCategoriaModels(categorias);
        
        when(useCase.buscarTodos()).thenReturn(categorias);
        when(mapper.toCollectionModel(categorias)).thenReturn(expected);
        
        assertTrue(controller.listar().size() > 0);
    }
}
