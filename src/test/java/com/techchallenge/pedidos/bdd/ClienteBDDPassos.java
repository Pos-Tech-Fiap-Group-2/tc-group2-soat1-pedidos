package com.techchallenge.pedidos.bdd;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.techchallenge.pedidos.core.domain.entities.Endereco;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.techchallenge.pedidos.adapter.driver.model.input.ClienteInput;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class ClienteBDDPassos {

	private Response response;
	
	private Long idCliente;

	private static String ENDPOINT_CLIENTES;
	
	static {
		Properties prop = new Properties();
		InputStream is = CategoriaBDDPassos.class.getResourceAsStream("/bdd-config.properties");
		
		try {
			prop.load(is);
			ENDPOINT_CLIENTES = prop.getProperty("bdd.endpoint.clientes.url");
			
		} catch (IOException e) {
			
		}
	}
	
	private ClienteInput createClienteInput(Long cpf, String email, String nome, Long telefone, Endereco endereco) {
		ClienteInput input = new ClienteInput();
		
		input.setCpf(cpf);
		input.setEmail(email);
		input.setNome(nome);
		input.setTelefone(telefone);
		input.setEndereco(endereco);
		
		return input;
	}
	
	private Response adicionarCliente(ClienteInput input) {
		return given().contentType(MediaType.APPLICATION_JSON_VALUE).body(input)
				.post(ENDPOINT_CLIENTES);
	}
	
	private Response consultarCliente(Long cpf) {
		return given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("cpf", cpf)
				.when()
				.get(ENDPOINT_CLIENTES + "/{cpf}");
	}
	
	private void removerClienteAposStep() {
		// Remove cliente registrado...
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idCliente)
				.when()
				.delete(ENDPOINT_CLIENTES + "/{id}");
	}
	
	@Quando("Solicitar a gravação dos dados do cliente")
	public void gravarCliente() {
		ClienteInput input = createClienteInput(12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 11965348752L, new Endereco());
		response = adicionarCliente(input);
	}

	@Então("o cliente é cadastrado com sucesso na plataforma.")
	public void clienteRegistradoComSucesso() {
		response.then().statusCode(HttpStatus.CREATED.value());
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idCliente = Long.valueOf(idValue);
		
		removerClienteAposStep();
	}

	@Dado("que um cliente já existente na plataforma")
	public void consultarClienteNaPlataforma() {
		ClienteInput input = createClienteInput(12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 11965348752L, new Endereco());
		response = adicionarCliente(input);
		
		response = consultarCliente(12345678901L);
		response.then().statusCode(HttpStatus.OK.value());
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idCliente = Long.valueOf(idValue);
	}

	@Quando("requisitar atualização dos dados do cliente")
	public void atualizarDadosDoCliente() {
		ClienteInput input = createClienteInput(12345678901L, "cliente-alterado.teste@teste.com.br", "Cliente Teste Alterado", 11965348756L, new Endereco());
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idCliente)
				.body(input)
				.when()
				.patch(ENDPOINT_CLIENTES + "/{id}");
	}

	@Então("cliente é atualizado com sucesso")
	public void clienteAtualizadoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
		
		removerClienteAposStep();
	}
	
	@Quando("solicitar os dados de um cliente")
	public void solicitarDadosDoCliente() {
		ClienteInput input = createClienteInput(12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 11965348752L, new Endereco());
		response = adicionarCliente(input);
		
		response = consultarCliente(12345678901L);
	}

	@Então("os dados devem ser apresentados com sucesso")
	public void dadosClienteRetornados() {
		response.then().statusCode(HttpStatus.OK.value());
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idCliente = Long.valueOf(idValue);
		
		removerClienteAposStep();
	}
	
	@Dado("que um cliente esteja cadastrado na plataforma")
	public void consultarCliente() {
		ClienteInput input = createClienteInput(21345678901L, "cliente-exclusao.teste@teste.com.br", "Cliente Teste Exclusao", 11965348752L, new Endereco());
		response = adicionarCliente(input);
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idCliente = Long.valueOf(idValue);
	}

	@Quando("for solicitado a remoção do cliente")
	public void requisitarRemocaoDoCliente() {
		removerClienteAposStep();
	}

	@Então("o cliente deve ser removido da plataforma")
	public void clienteRemovidoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
	}
}