package com.techchallenge.pedidos.adapter.mapper.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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

import com.techchallenge.pedidos.adapter.driver.model.CategoriaModel;
import com.techchallenge.pedidos.adapter.driver.model.input.CategoriaInput;
import com.techchallenge.pedidos.core.domain.entities.Categoria;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoriaApiMapperTest {

	@InjectMocks
	private CategoriaApiMapper api;
	
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
	
	private CategoriaModel createCategoriaModel(Categoria categoria) {
		CategoriaModel model = new CategoriaModel();
		
    	model.setId(categoria.getId());
    	model.setNome(categoria.getNome());
    	
    	return model;
	}
	
	private CategoriaInput createCategoriaInput(Categoria categoria) {
		CategoriaInput input = new CategoriaInput();
		
		input.setNome(categoria.getNome());
		
		return input;
	}
    
    @Test
    public void toDomainObject() {
    	Categoria categoria = createCategoria(1L, "Lanche");
    	CategoriaInput input = createCategoriaInput(categoria);
    	
    	when(mapper.map(input, Categoria.class)).thenReturn(categoria);
    	assertEquals(api.toDomainObject(input), categoria);
    }

    @Test
    public void toModel() {
    	Categoria categoria = createCategoria(1L, "Lanche");
    	CategoriaModel model = createCategoriaModel(categoria);
    	
    	when(mapper.map(categoria, CategoriaModel.class)).thenReturn(model);
        assertEquals(api.toModel(categoria), model);
    }

    @Test
    public void toCollectionModel() {
    	
    	List<Categoria> categorias = new ArrayList<>();
    	Categoria categoria = createCategoria(1L, "Lanche");
    	CategoriaModel model = createCategoriaModel(categoria);
    	
    	categorias.add(categoria);
    	
    	when(mapper.map(categoria, CategoriaModel.class)).thenReturn(model);
    	assertTrue(api.toCollectionModel(categorias).size() > 0);
    }
}
