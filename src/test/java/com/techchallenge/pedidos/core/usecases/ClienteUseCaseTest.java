package com.techchallenge.pedidos.core.usecases;

import com.techchallenge.pedidos.adapter.gateways.ClienteGateway;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.usecases.ClienteUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ClienteUseCaseTest {

    @InjectMocks
    private ClienteUseCase clienteUseCase;

    @Mock
    private ClienteGateway clienteGateway;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void devePermitirSalvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Teste");
        cliente.setEmail("teste@gmail.com");
        cliente.setCpf(12345678901L);

        when(clienteGateway.salvar(cliente)).thenReturn(cliente);

        Cliente responseCliente = clienteUseCase.salvar(cliente);
        assertEquals(cliente, responseCliente);

        verify(clienteGateway, times(1)).salvar(cliente);
    }

    @Test
    void devePermitirBuscarClientePorId() {
    	Long id = 1L;
    	Cliente cliente = new Cliente();
    	cliente.setId(id);
    	when(clienteGateway.buscarPorId(id)).thenReturn(cliente);

    	Cliente responseCliente = clienteUseCase.buscarPorId(id);
    	assertEquals(cliente, responseCliente);

    	verify(clienteGateway, times(1)).buscarPorId(id);
    }

    @Test
    void devePermirtirBuscarClientePorCpf() {
    	Long cpf = 12345678901L;
    	List<Cliente> clientes = Arrays.asList(new Cliente(), new Cliente());
    	when(clienteGateway.buscarPorCpf(cpf)).thenReturn(clientes);

        List<Cliente> responseClientes = clienteUseCase.buscarPorCpf(cpf);

        assertNotNull(responseClientes);
        assertEquals(clientes, responseClientes);

    	verify(clienteGateway, times(1)).buscarPorCpf(cpf);
    }

    @Test
    void devePermitirAtualizarCliente(){
        Long id = 1L;
        Cliente cliente = new Cliente();

        when(clienteGateway.buscarPorId(id)).thenReturn(cliente);
        Cliente responseCliente = clienteUseCase.buscarPorId(id);

        responseCliente.setNome("Teste Atualizar");
        responseCliente.setEmail("naopossui@gmail.com");

        assertDoesNotThrow(() -> clienteUseCase.atualizarDadosCliente(id, responseCliente));
        verify(clienteGateway, times(1)).atualizarDadosCliente(id, responseCliente);
    }

    @Test
    void devePermitirExcluirCliente() {
    	Long id = 1L;
    	Cliente cliente = new Cliente();
    	cliente.setId(id);
    	when(clienteGateway.buscarPorId(id)).thenReturn(cliente);

    	assertDoesNotThrow(() -> clienteUseCase.excluir(cliente.getId()));
    	verify(clienteGateway, times(1)).excluir(cliente.getId());
    }
}
