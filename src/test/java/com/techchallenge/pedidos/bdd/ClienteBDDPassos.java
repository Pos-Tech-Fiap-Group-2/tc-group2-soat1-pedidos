package com.techchallenge.pedidos.bdd;

import static io.restassured.RestAssured.given;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.techchallenge.pedidos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pedidos.utils.ResourceUtil;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class ClienteBDDPassos {

	private Response response;
	
	private Long idCliente;

	private final static String ENDPOINT_CLIENTES = "http://localhost:8080/api/clientes";
	
	private ClienteInput createClienteInput(Long cpf, String email, String nome) {
		ClienteInput input = new ClienteInput();
		
		input.setCpf(cpf);
		input.setEmail(email);
		input.setNome(nome);
		
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

	@Quando("Solicitar a gravação dos dados do cliente")
	public void gravarCliente() {
		ClienteInput input = createClienteInput(12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		response = adicionarCliente(input);
	}

	@Então("o cliente é cadastrado com sucesso na plataforma.")
	public void clienteRegistradoComSucesso() {
		response.then().statusCode(HttpStatus.CREATED.value());
	}

	@Dado("que um cliente já existente na plataforma")
	public void consultarClienteNaPlataforma() {
		response = consultarCliente(12345678901L);
		response.then().statusCode(HttpStatus.OK.value());
	}

	@Quando("requisitar atualização dos dados do cliente")
	public void atualizarDadosDoCliente() {
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/cliente-atualizacao-input-bdd.json");
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", 1)
				.body(content)
				.when()
				.patch(ENDPOINT_CLIENTES + "/{id}");
	}

	@Então("cliente é atualizado com sucesso")
	public void clienteAtualizadoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Quando("solicitar os dados de um cliente")
	public void solicitarDadosDoCliente() {
		response = consultarCliente(12345678901L);
	}

	@Então("os dados devem ser apresentados com sucesso")
	public void dadosClienteRetornados() {
		response.then().statusCode(HttpStatus.OK.value());
	}
	
	@Dado("que um cliente esteja cadastrado na plataforma")
	public void consultarCliente() {
		ClienteInput input = createClienteInput(21345678901L, "cliente-exclusao.teste@teste.com.br", "Cliente Teste Exclusao");
		response = adicionarCliente(input);
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idCliente = Long.valueOf(idValue);
	}

	@Quando("for solicitado a remoção do cliente")
	public void requisitarRemocaoDoCliente() {
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idCliente)
				.when()
				.delete(ENDPOINT_CLIENTES + "/{id}");
	}

	@Então("o cliente deve ser removido da plataforma")
	public void clienteRemovidoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
	}
}