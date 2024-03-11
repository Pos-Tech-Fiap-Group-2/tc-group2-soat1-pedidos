package com.techchallenge.pedidos.adapter.mapper.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.techchallenge.pedidos.core.domain.entities.Endereco;
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

    private Cliente createCliente(Long id, Long cpf, String email, String nome, Long telefone, Boolean ativo, Endereco endereco) {
        Cliente cliente = new Cliente();

        cliente.setId(id);
        cliente.setCpf(cpf);
        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        cliente.setEndereco(endereco);
        cliente.setAtivo(ativo);

        return cliente;
    }

    private ClienteInput createClienteInput(Cliente cliente) {
        ClienteInput input = new ClienteInput();

        input.setCpf(cliente.getCpf());
        input.setNome(cliente.getNome());
        input.setTelefone(cliente.getTelefone());
        input.setEmail(cliente.getEmail());
        input.setEndereco(cliente.getEndereco());

        return input;
    }

    private ClienteAtualizacaoInput createClienteAtualizacaoInput(Cliente cliente) {
        ClienteAtualizacaoInput input = new ClienteAtualizacaoInput();

        input.setNome(cliente.getNome());
        input.setEmail(cliente.getEmail());
        input.setTelefone(cliente.getTelefone());
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
        model.setEndereco(cliente.getEndereco());
        model.setAtivo(cliente.getAtivo());

        return model;
    }

    @Test
    public void toDomainObject_inclusao() {
        Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 12345678901L, true, new Endereco());
        ClienteInput input = createClienteInput(cliente);

        when(mapper.map(input, Cliente.class)).thenReturn(cliente);
        assertEquals(api.toDomainObject(input), cliente);
    }

    @Test
    public void toDomainObject_atualizacao() {
        Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 12345678901L, true, new Endereco());
        ClienteAtualizacaoInput input = createClienteAtualizacaoInput(cliente);

        when(mapper.map(input, Cliente.class)).thenReturn(cliente);
        assertEquals(api.toDomainObject(input), cliente);
    }

    @Test
    public void toModel() {
        Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 12345678901L, true, new Endereco());
        ClienteModel model = createClienteModel(cliente);

        when(mapper.map(cliente, ClienteModel.class)).thenReturn(model);
        assertEquals(api.toModel(cliente), model);
    }

    @Test
    public void toCollectionModel() {
        Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 12345678901L, true, new Endereco());
        ClienteModel model = createClienteModel(cliente);

        List<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente);

        when(mapper.map(cliente, ClienteModel.class)).thenReturn(model);
        assertTrue(api.toCollectionModel(clientes).size() > 0);
    }
}
