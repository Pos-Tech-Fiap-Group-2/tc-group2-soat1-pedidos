package com.techchallenge.pedidos.core.usecases;

import com.techchallenge.pedidos.adapter.gateways.CategoriaGateway;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.usecases.CategoriaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoriaUseCaseTest {

    @InjectMocks
    private CategoriaUseCase categoriaUseCase;

    @Mock
    private CategoriaGateway categoriaGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void devePermitirListarCategorias() {

        List<Categoria> categorias = Arrays.asList(new Categoria(), new Categoria());
        when(categoriaGateway.buscarTodos()).thenReturn(categorias);

        List<Categoria> responseCategoria = categoriaUseCase.buscarTodos();
        assertEquals(categorias, responseCategoria);

        verify(categoriaGateway, times(1)).buscarTodos();

    }

    @Test
    void devePermitirBuscarCategoriaPorId() {
    	Long id = 1L;
    	Categoria categoria = new Categoria();
    	categoria.setId(id);
    	when(categoriaGateway.buscarPorId(id)).thenReturn(categoria);

    	Categoria responseCategoria = categoriaUseCase.buscarPorId(id);
    	assertEquals(categoria, responseCategoria);

    	verify(categoriaGateway, times(1)).buscarPorId(id);
    }
}
