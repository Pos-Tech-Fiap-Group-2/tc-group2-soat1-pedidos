package com.techchallenge.pedidos.bdd;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class CategoriaBDDPassos {

	private Response response;

	private static String ENDPOINT_CATEGORIAS;
	
	static {
		Properties prop = new Properties();
		InputStream is = CategoriaBDDPassos.class.getResourceAsStream("/bdd-config.properties");
		
		try {
			prop.load(is);
			ENDPOINT_CATEGORIAS = prop.getProperty("bdd.endpoint.categorias.url");
			
		} catch (IOException e) {
			
		}
	}
	
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
