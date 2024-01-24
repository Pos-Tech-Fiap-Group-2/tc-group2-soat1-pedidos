package com.techchallenge.pedidos.bdd;

import static io.restassured.RestAssured.given;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class CategoriaBDDPassos {

	private Response response;

	private String ENDPOINT_CATEGORIAS = "http://localhost:8080/api/categorias";
	
	@Quando("Solicitar todas as categorias")
	public void adicionarPedidoAFila() {
		response = given().contentType(MediaType.APPLICATION_JSON_VALUE).when()
				.get(ENDPOINT_CATEGORIAS);
	}

	@Então("a lista de categorias é retornada com sucesso.")
	public void pedidoRegistradoComSucesso() {
		response.then().statusCode(HttpStatus.OK.value());
	}
}
