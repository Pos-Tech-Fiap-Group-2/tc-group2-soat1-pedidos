package com.techchallenge.pedidos.adapter.mapper.business;

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

import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoriaBusinessMapperTest {

	@InjectMocks
	private CategoriaBusinessMapper api;
	
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
	
	private CategoriaEntity createCategoriaEntity(Categoria categoria) {
		CategoriaEntity entity = new CategoriaEntity();
		
		entity.setId(categoria.getId());
		entity.setNome(categoria.getNome());
		
		return entity;
	}
    
    @Test
	public void toModel() {
    	Categoria categoria = createCategoria(1L, "Lanche");
    	CategoriaEntity entity = createCategoriaEntity(categoria);
    	
    	when(mapper.map(entity, Categoria.class)).thenReturn(categoria);
    	assertEquals(api.toModel(entity), categoria);
	}

    @Test
    public void toCollectionModel() {
    	List<CategoriaEntity> entities = new ArrayList<>();
    	Categoria categoria = createCategoria(1L, "Lanche");
    	CategoriaEntity entity = createCategoriaEntity(categoria);
    	
    	entities.add(entity);
    	
    	when(mapper.map(entity, Categoria.class)).thenReturn(categoria);
    	assertTrue(api.toCollectionModel(entities).size() > 0);
    }
}
