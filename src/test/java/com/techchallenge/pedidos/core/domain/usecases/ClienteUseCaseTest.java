package com.techchallenge.pedidos.core.domain.usecases;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

import com.techchallenge.pedidos.adapter.gateways.ClienteGateway;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.exception.NegocioException;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClienteUseCaseTest {

	@InjectMocks
	private ClienteUseCase useCase;
	
	@Mock
    private ClienteGateway gateway;
	
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
	
	private Cliente createCliente(Long id, Long cpf, String email, String nome) {
		Cliente cliente = new Cliente();
		
		cliente.setCpf(cpf);
		cliente.setEmail(email);
		cliente.setId(id);
		cliente.setNome(nome);
		
		return cliente;
	}
	
	@SuppressWarnings("serial")
	private List<Cliente> createClientes() {
		return new ArrayList<Cliente>() {{
			this.add(createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"));
		}};
	}
	
	@Test
    public void salvar() {
		Cliente entity = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Cliente expected = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		
    	when(gateway.salvar(entity)).thenReturn(expected);
    	assertEquals(useCase.salvar(entity), expected);
    }
    
	@Test
    public void buscarPorId() {
		Long id = 1L;
		Cliente expected = createCliente(id, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		
    	when(gateway.buscarPorId(id)).thenReturn(expected);
    	assertEquals(useCase.buscarPorId(id), expected);
    }

	@Test
    public void buscarPorCpf() {
		Long cpf = 12345678901L;
		List<Cliente> expected = createClientes();
		
        when(gateway.buscarPorCpf(cpf)).thenReturn(expected);
        assertTrue(useCase.buscarPorCpf(cpf).size() == 1);
    }
    
	@Test
    public void excluir() {
		Long id = 1L;
		
    	doNothing().when(gateway).excluir(id);
    	assertDoesNotThrow(() -> useCase.excluir(id));
    }
    
	@Test
    public void atualizarDadosCliente() {
		Long id = 1L;
		Cliente expected = createCliente(id, 12345678902L, "cliente_novo.teste@teste.com.br", "Cliente Novo Teste");
		
    	doNothing().when(this.gateway).atualizarDadosCliente(id, expected);
    	assertDoesNotThrow(() -> useCase.atualizarDadosCliente(id, expected));
    }
	
	@Test
    public void salvar_then_throws_NegocioException() {
		Cliente entity = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		
    	when(gateway.salvar(entity)).thenThrow(NegocioException.class);
    	assertThrows(NegocioException.class, () -> useCase.salvar(entity));
    }
	
	@Test
    public void excluir_then_throws_NegocioException() {
		Long id = 1L;
		
		doThrow(NegocioException.class).when(gateway).excluir(id);
		assertThrows(NegocioException.class, () -> useCase.excluir(id));
    }
	
	@Test
    public void atualizarDadosCliente_then_throws_NegocioException() {
		Long id = 1L;
		Cliente expected = createCliente(id, 12345678902L, "cliente_novo.teste@teste.com.br", "Cliente Novo Teste");
		
		doThrow(NegocioException.class).when(gateway).atualizarDadosCliente(id, expected);
		assertThrows(NegocioException.class, () -> useCase.atualizarDadosCliente(id, expected));
    }
}
