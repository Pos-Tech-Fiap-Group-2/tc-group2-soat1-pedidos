package com.techchallenge.pedidos.adapter.gateways.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.techchallenge.pedidos.adapter.mapper.business.ClienteBusinessMapper;
import com.techchallenge.pedidos.adapter.mapper.db.ClienteEntityMapper;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.exception.EntidadeEmUsoException;
import com.techchallenge.pedidos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pedidos.core.domain.exception.NegocioException;
import com.techchallenge.pedidos.drivers.db.entities.ClienteEntity;
import com.techchallenge.pedidos.drivers.db.repositories.ClienteRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClienteGatewayImplTest {

	@InjectMocks
	private ClienteGatewayImpl gateway;
	
    @Mock
    private ClienteRepository repository;
    @Mock
    private ClienteEntityMapper mapper;
    @Mock
    private ClienteBusinessMapper businessMapper;
    
    @BeforeEach
    private void setup() {
    	MockitoAnnotations.initMocks(this);
    }
    
	private ClienteEntity createClienteEntity(Long id, Long cpf, String email, String nome) {
		ClienteEntity cliente = new ClienteEntity();
		
		cliente.setCpf(cpf);
		cliente.setEmail(email);
		cliente.setId(id);
		cliente.setNome(nome);
		
		return cliente;
	}
	
	@SuppressWarnings("serial")
	private List<ClienteEntity> createClienteEntities() {
		return new ArrayList<ClienteEntity>() {{
			this.add(createClienteEntity(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste"));
		}};
	}
    
	private Cliente createCliente(ClienteEntity entity) {
		Cliente cliente = new Cliente();
		
		cliente.setCpf(entity.getCpf());
		cliente.setEmail(entity.getEmail());
		cliente.setId(entity.getId());
		cliente.setNome(entity.getNome());
		
		return cliente;
	}
	
	private List<Cliente> createClientes(List<ClienteEntity> entities) {
		return entities.stream().map(e -> createCliente(e)).collect(Collectors.toList());
	}
    
    @Test
    public void salvar() {
    	Long cpf = 12345678901L;
    	String email = "cliente.teste@teste.com.br";
    	
    	ClienteEntity clienteEntity = createClienteEntity(1L, cpf, email, "Cliente Teste");
    	ClienteEntity expectedEntity = createClienteEntity(1L, cpf, email, "Cliente Teste");
    	Cliente cliente = createCliente(expectedEntity);
    	Cliente expected = createCliente(expectedEntity);
    	
    	when(repository.findByCpfOrEmail(cpf, email)).thenReturn(List.of());
    	when(repository.save(clienteEntity)).thenReturn(expectedEntity);
    	
    	when(mapper.toModel(cliente)).thenReturn(expectedEntity);
    	when(businessMapper.toModel(expectedEntity)).thenReturn(expected);
    	
    	assertEquals(this.gateway.salvar(cliente), expected);
    }
    
    @Test
    public void buscarPorId() {
    	Long cpf = 12345678901L;
    	String email = "cliente.teste@teste.com.br";
    	
    	Optional<ClienteEntity> expectedEntity = Optional.of(createClienteEntity(1L, cpf, email, "Cliente Teste"));
    	Cliente expected = createCliente(expectedEntity.get());
    	
    	when(repository.findById(expectedEntity.get().getId())).thenReturn(expectedEntity);
    	when(businessMapper.toModel(expectedEntity.get())).thenReturn(expected);
    	
    	assertEquals(this.gateway.buscarPorId(1L), expected);
    }

    @Test
    public void buscarPorCpf() {
    	Long cpf = 12345678901L;
    	String email = "cliente.teste@teste.com.br";
    	
    	List<ClienteEntity> expectedEntities = createClienteEntities();
    	List<Cliente> expected = createClientes(expectedEntities);
    	
    	Optional<ClienteEntity> expectedEntity = Optional.of(createClienteEntity(1L, cpf, email, "Cliente Teste"));
    	
    	when(repository.findByCpfIs(expectedEntity.get().getCpf())).thenReturn(expectedEntities);
    	when(businessMapper.toCollectionModel(expectedEntities)).thenReturn(expected);
    	
    	assertEquals(this.gateway.buscarPorCpf(cpf), expected);
    }
    
    @Test
    public void excluir() {
    	doNothing().when(repository).deleteById(1L);
    	assertDoesNotThrow(() -> this.gateway.excluir(1L));
    }
    
    @Test
    public void atualizarDadosCliente() {
    	Long cpf = 12345678901L;
    	String email = "cliente.teste@teste.com.br";
    	
    	Optional<ClienteEntity> expectedEntity = Optional.of(createClienteEntity(1L, cpf, email, "Cliente Teste"));
    	Cliente expected = createCliente(expectedEntity.get());
    	
    	when(repository.findById(expectedEntity.get().getId())).thenReturn(expectedEntity);
    	assertDoesNotThrow(() -> this.gateway.atualizarDadosCliente(1L, expected));
    }
    
    @Test
    public void salvar_then_throws_NegocioException() {
    	Long cpf = 12345678901L;
    	String email = "cliente.teste@teste.com.br";
    	
    	ClienteEntity expectedEntity = createClienteEntity(1L, cpf, email, "Cliente Teste");
    	Cliente cliente = createCliente(expectedEntity);
    	
    	List<ClienteEntity> expectedEntities = createClienteEntities();
    	
    	when(repository.findByCpfOrEmail(cpf, email)).thenReturn(expectedEntities);
    	assertThrows(NegocioException.class, () -> this.gateway.salvar(cliente));
    }
    
    @Test
    public void buscarPorId_then_throws_NegocioException() {
    	Long cpf = 12345678901L;
    	String email = "cliente.teste@teste.com.br";
    	
    	ClienteEntity entity = createClienteEntity(1L, cpf, email, "Cliente Teste");
    	Optional<ClienteEntity> expectedEntity = Optional.empty();
    	
    	when(repository.findById(entity.getId())).thenReturn(expectedEntity);
    	assertThrows(NegocioException.class, () -> this.gateway.buscarPorId(1L));
    }
    
    @Test
    public void excluir_then_throws_Exception() {
    	doThrow(DataIntegrityViolationException.class).when(repository).deleteById(1L);
    	assertThrows(EntidadeEmUsoException.class, () -> this.gateway.excluir(1L));
    	
    	doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(1L);
    	assertThrows(EntidadeNaoEncontradaException.class, () -> this.gateway.excluir(1L));
    }
    
    @Test
    public void atualizarDadosCliente_then_throws_EntidadeNaoEncontradaException() {
    	Long cpf = 12345678901L;
    	String email = "cliente.teste@teste.com.br";
    	
    	Optional<ClienteEntity> expectedEntity = Optional.empty();
    	ClienteEntity entity = createClienteEntity(1L, cpf, email, "Cliente Teste");
    	Cliente expected = createCliente(entity);
    	
    	when(repository.findById(entity.getId())).thenReturn(expectedEntity);
    	assertThrows(EntidadeNaoEncontradaException.class, () -> this.gateway.atualizarDadosCliente(1L, expected));
    }
}
