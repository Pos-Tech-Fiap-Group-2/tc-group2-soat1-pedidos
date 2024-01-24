package com.techchallenge.pedidos.core.domain.usecases;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.gateways.CategoriaGateway;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.exception.NegocioException;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoriaUseCaseTest {

	@InjectMocks
	private CategoriaUseCase useCase;
	
	@Mock
    private CategoriaGateway gateway;
	
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
	
	@Test
    public void buscarTodos() {
		when(gateway.buscarTodos()).thenReturn(createCategorias());
		assertTrue(this.useCase.buscarTodos().size() > 0);
    }
    
	@Test
    public void buscarPorId() {
		Long id = 1L;
		Categoria expected = createCategoria(1L, "Lanche");
		
		when(gateway.buscarPorId(id)).thenReturn(expected);
		assertEquals(this.useCase.buscarPorId(id), expected);
    }
	
	@Test
    public void buscarPorId_throws_NegocioException() {
		Long id = 1L;
		
		when(gateway.buscarPorId(id)).thenThrow(NegocioException.class);
		assertThrows(NegocioException.class, () -> this.useCase.buscarPorId(id));
    }
}
