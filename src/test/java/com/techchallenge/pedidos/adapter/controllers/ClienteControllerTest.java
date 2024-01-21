package com.techchallenge.pedidos.adapter.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
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

import com.techchallenge.pedidos.adapter.driver.model.ClienteModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ClienteAtualizacaoInput;
import com.techchallenge.pedidos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pedidos.adapter.mapper.api.ClienteApiMapper;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.usecases.ClienteUseCase;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClienteControllerTest {
	
	@InjectMocks
	private ClienteController controller;

	@Mock
	private ClienteUseCase useCase;
	@Mock
	private ClienteApiMapper mapper;
	
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
	
	private ClienteInput createClienteInput(Cliente cliente) {
		ClienteInput input = new ClienteInput();
		
		input.setCpf(cliente.getCpf());
		input.setEmail(cliente.getEmail());
		input.setNome(cliente.getNome());
		
		return input;
	}
	
	private ClienteAtualizacaoInput createClienteAtualizacaoInput(Cliente cliente) {
		ClienteAtualizacaoInput input = new ClienteAtualizacaoInput();
		
		input.setEmail(cliente.getEmail());
		input.setNome(cliente.getNome());
		
		return input;
	}
	
	private ClienteModel createClienteModel(Cliente cliente) {
		ClienteModel model = new ClienteModel();
		
		model.setCpf(cliente.getCpf());
		model.setEmail(cliente.getEmail());
		model.setId(cliente.getId());
		model.setNome(cliente.getNome());
		
		return model;
	}
	
	@SuppressWarnings("serial")
	private List<Cliente> createClientes() {
		return new ArrayList<Cliente>() {{
			this.add(createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"));
		}};
	}
	
	private List<ClienteModel> createClienteModels(List<Cliente> clientes) {
		return clientes.stream().map(c -> createClienteModel(c)).collect(Collectors.toList());
	}
    
    @Test
	public void adicionar() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	Cliente expected = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	ClienteInput input = createClienteInput(cliente);
    	ClienteModel expectedModel = createClienteModel(cliente);
    	
    	when(mapper.toDomainObject(input)).thenReturn(cliente);
    	when(useCase.salvar(cliente)).thenReturn(expected);
    	when(mapper.toModel(cliente)).thenReturn(expectedModel);
    	
    	assertNotNull(controller.adicionar(input));
	}

    @Test
	public void buscarPorCpf() {
    	List<Cliente> clientes = createClientes();
    	List<ClienteModel> expectedModels = createClienteModels(clientes);
    	Long cpf = clientes.stream().findFirst().get().getCpf();
    	
    	when(useCase.buscarPorCpf(cpf)).thenReturn(clientes);
    	when(mapper.toCollectionModel(clientes)).thenReturn(expectedModels);

    	assertEquals(controller.buscarPorCpf(cpf), expectedModels.stream().findFirst().get());
	}
	
    @Test
	public void remover() {
    	Long cpf = 12345678901L;
    	
    	doNothing().when(useCase).excluir(cpf);
    	assertDoesNotThrow(() -> controller.remover(cpf));
	}
	
    @Test
    public void atualizarDadosCliente() {
    	Long id = 1L;
    	Cliente cliente = createCliente(id, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	ClienteAtualizacaoInput input = createClienteAtualizacaoInput(cliente);
    	
    	when(mapper.toDomainObject(input)).thenReturn(cliente);
    	doNothing().when(useCase).atualizarDadosCliente(id, cliente);
    	
    	assertDoesNotThrow(() -> controller.atualizarDadosCliente(id, input));
    }
}
