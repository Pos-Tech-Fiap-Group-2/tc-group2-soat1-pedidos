package com.techchallenge.pedidos.adapter.mapper.db;

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

import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.drivers.db.entities.ClienteEntity;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClienteEntityMapperTest {

	@InjectMocks
	private ClienteEntityMapper api;
	
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
	
	private ClienteEntity createClienteEntity(Cliente cliente) {
		ClienteEntity entity = new ClienteEntity();
		
		entity.setCpf(cliente.getCpf());
		entity.setEmail(cliente.getEmail());
		entity.setId(cliente.getId());
		entity.setNome(cliente.getNome());
		
		return entity;
	}
    
    @Test
	public void toModel() {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	ClienteEntity entity = createClienteEntity(cliente);
    	
    	when(mapper.map(cliente, ClienteEntity.class)).thenReturn(entity);
		assertEquals(api.toModel(cliente), entity);
	}
	
    @Test
	public void toCollectionModel() {
    	List<Cliente> clientes = new ArrayList<>();
    	
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
    	ClienteEntity entity = createClienteEntity(cliente);
    	
    	clientes.add(cliente);
    	
    	when(mapper.map(cliente, ClienteEntity.class)).thenReturn(entity);
    	assertTrue(api.toCollectionModel(clientes).size() > 0);
	}
}
