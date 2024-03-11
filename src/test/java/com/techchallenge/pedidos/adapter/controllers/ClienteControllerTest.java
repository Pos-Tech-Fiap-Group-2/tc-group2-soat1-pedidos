package com.techchallenge.pedidos.adapter.controllers;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.techchallenge.pedidos.core.domain.entities.Endereco;
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

    private Cliente createCliente(Long id, Long cpf, String email, String nome, Long telefone, Boolean ativo, Endereco endereco) {
        Cliente cliente = new Cliente();

        cliente.setId(id);
        cliente.setCpf(cpf);
        cliente.setNome(nome);
        cliente.setEmail(email);
        cliente.setTelefone(telefone);
        cliente.setAtivo(ativo);
        cliente.setEndereco(endereco);

        return cliente;
    }

    private ClienteInput createClienteInput(Cliente cliente) {
        ClienteInput input = new ClienteInput();

        input.setCpf(cliente.getCpf());
        input.setNome(cliente.getNome());
        input.setEmail(cliente.getEmail());
        input.setTelefone(cliente.getTelefone());
        input.setEndereco(cliente.getEndereco());

        return input;
    }

    private ClienteAtualizacaoInput createClienteAtualizacaoInput(Cliente cliente) {
        ClienteAtualizacaoInput input = new ClienteAtualizacaoInput();

        input.setNome(cliente.getNome());
        input.setTelefone(cliente.getTelefone());
        input.setEmail(cliente.getEmail());
        input.setEndereco(cliente.getEndereco());
        input.setAtivo(cliente.getAtivo());

        return input;
    }

    private ClienteModel createClienteModel(Cliente cliente) {
        ClienteModel model = new ClienteModel();

        model.setId(cliente.getId());
        model.setCpf(cliente.getCpf());
        model.setNome(cliente.getNome());
        model.setTelefone(cliente.getTelefone());
        model.setEmail(cliente.getEmail());
        model.setTelefone(cliente.getTelefone());
        model.setEndereco(cliente.getEndereco());
        model.setAtivo(cliente.getAtivo());

        return model;
    }

    @SuppressWarnings("serial")
    private List<Cliente> createClientes() {
        return new ArrayList<Cliente>() {
            {
                Endereco endereco = new Endereco();
                endereco.setCep("12345-678");
                endereco.setCidade("São Paulo");
                endereco.setLogradouro("Rua das Flores");
                endereco.setNumero(2345L);
                endereco.setComplemento("Centro");
                this.add(createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 11965348752L, true, endereco));
            }
        };
    }

    private List<ClienteModel> createClienteModels(List<Cliente> clientes) {
        return clientes.stream().map(c -> createClienteModel(c)).collect(Collectors.toList());
    }

    @Test
    public void adicionar() {
        Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 11965348752L, true, new Endereco());
        Cliente expected = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 11965348752L, true, new Endereco());
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
        Cliente cliente = createCliente(id, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 11965348752L, true, new Endereco());
        ClienteAtualizacaoInput input = createClienteAtualizacaoInput(cliente);

        when(mapper.toDomainObject(input)).thenReturn(cliente);
        doNothing().when(useCase).atualizarDadosCliente(id, cliente);

        assertDoesNotThrow(() -> controller.atualizarDadosCliente(id, input));
    }
}
