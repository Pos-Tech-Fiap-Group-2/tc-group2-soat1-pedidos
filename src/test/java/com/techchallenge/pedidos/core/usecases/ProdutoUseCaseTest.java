package com.techchallenge.pedidos.core.usecases;

import com.techchallenge.pedidos.adapter.gateways.ProdutoGateway;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.usecases.CategoriaUseCase;
import com.techchallenge.pedidos.core.domain.usecases.ProdutoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProdutoUseCaseTest {

    @InjectMocks
    private ProdutoUseCase produtoUseCase;

    @Mock
    private ProdutoGateway produtoGateway;
    @Mock
    private CategoriaUseCase categoriaUseCase;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void devePermitirSalvarProduto(){
        Produto produto = new Produto();
        Categoria categoria = new Categoria();
        categoria.setId(1L);

        when(categoriaUseCase.buscarPorId(1L)).thenReturn(categoria);

        when(produtoGateway.salvar(produto)).thenReturn(produto);
        produto.setCategoria(categoria);
        Produto responseProduto = produtoUseCase.salvar(produto);

        assertNotNull(responseProduto);
        assertEquals(responseProduto.getCategoria().getId(), categoria.getId());
        verify(categoriaUseCase, times(1)).buscarPorId(1L);

        assertEquals(responseProduto, produto);
        verify(produtoGateway, times(1)).salvar(produto);
    }

    @Test
    void devePermitirBuscarProdutos(){
        List<Produto> produtos = new ArrayList<>();

        when(produtoGateway.buscarTodos()).thenReturn(produtos);

        List<Produto> responseProdutos = produtoUseCase.buscarTodos();

        assertNotNull(responseProdutos);
        assertEquals(responseProdutos, produtos);
        verify(produtoGateway, times(1)).buscarTodos();

    }

    @Test
    void devePermitirBuscarProdutosPorCategoria(){
        String categoriaProduto = "Acompanhamento";

        List<Produto> produtos = new ArrayList<>();
        Categoria categoria = new Categoria();
        categoria.setNome(categoriaProduto);
        when(produtoGateway.buscarPorCategoria(categoriaProduto)).thenReturn(produtos);

        when(produtoGateway.buscarPorCategoria(categoriaProduto)).thenReturn(produtos);

        List<Produto> responseProdutos = produtoUseCase.buscarPorCategoria(1L);

        assertNotNull(responseProdutos);
        assertEquals(responseProdutos, produtos);
        verify(produtoGateway, times(1)).buscarPorCategoria(1L);

    }

    @Test
    void devePermitirBuscarProdutosPorCategoriaId(){
        Long categoriaId = 1L;

        List<Produto> produtos = new ArrayList<>();
        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        when(produtoGateway.buscarPorCategoria(categoriaId)).thenReturn(produtos);

        List<Produto> responseProdutos = produtoUseCase.buscarPorCategoria(1L);

        assertNotNull(responseProdutos);
        assertEquals(produtos, responseProdutos);
        verify(produtoGateway, times(1)).buscarPorCategoria(1L);

    }

    @Test
    void devePermitirAtualizarProduto(){
        Long produtoId = 1L;
        Produto produto = new Produto();
        produto.setId(produtoId);
        Categoria categoria = new Categoria();
        categoria.setId(1L);
        produto.setCategoria(categoria);

        when(produtoGateway.salvar(produto)).thenReturn(produto);
        Produto responseProduto = produtoUseCase.salvar(produto);

        responseProduto.setCategoria(categoria);
        responseProduto.setNome("Produto Atualizado");
        responseProduto.setDescricao("Descrição Atualizada");
        responseProduto.setImagem("Imagem Atualizada");
        responseProduto.setPreco(new BigDecimal(10.00));

        assertDoesNotThrow(() -> produtoUseCase.atualizar(produtoId, responseProduto));

        verify(produtoGateway, times(1)).atualizar(produtoId, responseProduto);
    }

    @Test
    void devePermitirExcluirProduto(){
        Long produtoId = 1L;
        assertDoesNotThrow(() -> produtoUseCase.excluir(produtoId));
        verify(produtoGateway, times(1)).excluir(produtoId);
    }

}
