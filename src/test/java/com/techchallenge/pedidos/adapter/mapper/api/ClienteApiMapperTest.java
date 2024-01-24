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

import com.techchallenge.pedidos.adapter.driver.model.ClienteModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ClienteAtualizacaoInput;
import com.techchallenge.pedidos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pedidos.core.domain.entities.Cliente;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClienteApiMapperTest {
	
	@InjectMocks
	private ClienteApiMapper api;
	
	@Mock
	private ModelMapper mapper;
	
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
	
    @Test
	public void toDomainObject_inclusao() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	ClienteInput input = createClienteInput(cliente);
    	
    	when(mapper.map(input, Cliente.class)).thenReturn(cliente);
		assertEquals(api.toDomainObject(input), cliente);
	}
	
    @Test
	public void toDomainObject_atualizacao() {
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		ClienteAtualizacaoInput input = createClienteAtualizacaoInput(cliente);
		
    	when(mapper.map(input, Cliente.class)).thenReturn(cliente);
		assertEquals(api.toDomainObject(input), cliente);
	}
	
    @Test
	public void toModel() {
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		ClienteModel model = createClienteModel(cliente);
		
		when(mapper.map(cliente, ClienteModel.class)).thenReturn(model);
		assertEquals(api.toModel(cliente), model);
	}
	
    @Test
	public void toCollectionModel() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	ClienteModel model = createClienteModel(cliente);
    	
    	List<Cliente> clientes = new ArrayList<>();
    	clientes.add(cliente);
    	
    	when(mapper.map(cliente, ClienteModel.class)).thenReturn(model);
    	assertTrue(api.toCollectionModel(clientes).size() > 0);
	}
}
