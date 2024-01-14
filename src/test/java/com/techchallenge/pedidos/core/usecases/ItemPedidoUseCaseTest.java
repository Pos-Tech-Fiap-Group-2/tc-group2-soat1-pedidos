package com.techchallenge.pedidos.core.usecases;

import com.techchallenge.pedidos.adapter.gateways.ItemPedidoGateway;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.usecases.ItemPedidoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ItemPedidoUseCaseTest {

    @InjectMocks
    private ItemPedidoUseCase itemPedidoUseCase;

    @Mock
    private ItemPedidoGateway itemPedidoGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void devePermitirAdicionarItemAoPedido() {
    	Long id = 1L;
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(id);
        when(itemPedidoGateway.adicionarItemAoPedido(id, itemPedido)).thenReturn(itemPedido);

        ItemPedido responseItemPedido = itemPedidoUseCase.adicionarItemAoPedido(id, itemPedido);

        assertNotNull(responseItemPedido);
        assertEquals(itemPedido, responseItemPedido);
        verify(itemPedidoGateway, times(1)).adicionarItemAoPedido(id, itemPedido);
    }

    @Test
    void devePermitirAtualizarItemAoPedido() {
    	Long id = 1L;
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(id);
        when(itemPedidoGateway.adicionarItemAoPedido(id, itemPedido)).thenReturn(itemPedido);
        ItemPedido responseItemPedido = itemPedidoUseCase.adicionarItemAoPedido(id, itemPedido);

        responseItemPedido.setQuantidade(2);
        responseItemPedido.setProduto(new Produto());

        assertDoesNotThrow(() -> itemPedidoUseCase.atualizarItemAoPedido(id, responseItemPedido));

        verify(itemPedidoGateway, times(1)).atualizarItemAoPedido(id, itemPedido);

    }

    @Test
    void devePermitirExcluirItemAoPedido() {
    	Long itemId = 1L;
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setId(itemId);

        Long produtoId = 1L;
        Produto produto = new Produto();
        produto.setId(produtoId);

        assertDoesNotThrow(() -> itemPedidoUseCase.excluirItemAoPedido(itemId, produtoId));

        verify(itemPedidoGateway, times(1)).excluirItemAoPedido(itemId, produtoId);

    }

}
