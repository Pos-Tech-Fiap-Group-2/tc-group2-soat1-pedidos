package com.techchallenge.pedidos.adapter.gateways.impl;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

import com.techchallenge.pedidos.adapter.mapper.business.CategoriaBusinessMapper;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;
import com.techchallenge.pedidos.drivers.db.repositories.CategoriaRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoriaGatewayImplTest {

	@InjectMocks
	private CategoriaGatewayImpl gateway;
	
    @Mock
    private CategoriaRepository repository;

    @Mock
    private CategoriaBusinessMapper mapper;
    
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
    
    @SuppressWarnings("serial")
	private List<CategoriaEntity> createCategoriaEntities() {
    	
    	return new ArrayList<CategoriaEntity>() {{
    		this.add(createCategoriaEntity(1L, "Lanche"));
    		this.add(createCategoriaEntity(2L, "Bebida"));
    		this.add(createCategoriaEntity(3L, "Acompanhamento"));
    		this.add(createCategoriaEntity(4L, "Sobremesa"));
    	}};
    }    	

	private CategoriaEntity createCategoriaEntity(Long id, String nome) {
		CategoriaEntity categoria = new CategoriaEntity();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
	}
	
	private List<Categoria> createCategorias(List<CategoriaEntity> entities) {
    	return entities.stream().map(e -> createCategoria(e)).collect(Collectors.toList());
    }    	

	private Categoria createCategoria(CategoriaEntity entity) {
		Categoria categoria = new Categoria();
		
    	categoria.setId(entity.getId());
    	categoria.setNome(entity.getNome());
    	
    	return categoria;
	}
    
    @Test
    public void buscarTodos() {
    	List<CategoriaEntity> expectedEntities = createCategoriaEntities();
    	List<Categoria> expected = createCategorias(expectedEntities);
    	
    	when(repository.findAll()).thenReturn(expectedEntities);
    	when(mapper.toCollectionModel(expectedEntities)).thenReturn(expected);
    	
    	assertTrue(this.gateway.buscarTodos().size() > 0);
    }

	@Test
	public void buscarPorId() {
		Optional<CategoriaEntity> expectedEntity = Optional.ofNullable(createCategoriaEntity(1L, "Lanche"));
		Categoria categoria = createCategoria(expectedEntity.get());
		Categoria expected = createCategoria(expectedEntity.get());
		
		when(repository.findById(1L)).thenReturn(expectedEntity);
		when(mapper.toModel(expectedEntity.get())).thenReturn(expected);

		assertEquals(this.gateway.buscarPorId(categoria.getId()), expected);
	}
	
	@Test
	public void buscarPorId_then_throw_EntidadeNaoEncontradaException() {
		Optional<CategoriaEntity> expectedEntity = Optional.empty();
		
		when(repository.findById(1L)).thenReturn(expectedEntity);
		assertThrows(EntidadeNaoEncontradaException.class, () -> this.gateway.buscarPorId(1L));
	}
}
