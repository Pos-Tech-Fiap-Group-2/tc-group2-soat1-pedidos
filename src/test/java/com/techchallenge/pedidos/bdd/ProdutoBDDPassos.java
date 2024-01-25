package com.techchallenge.pedidos.bdd;

import static io.restassured.RestAssured.given;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.techchallenge.pedidos.adapter.driver.model.input.ProdutoInput;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class ProdutoBDDPassos {

	private Response response;
	
	private Long idCategoria;
	
	private String nomeCategoria;
	
	private Long idProduto;

	private final static String ENDPOINT_PRODUTOS = "http://localhost:8080/api/produtos";
	
	private ProdutoInput createProdutoInput(String descricao, String imagem, String nome, BigDecimal preco, Long categoriaId) {
		ProdutoInput produto = new ProdutoInput();
		
		produto.setCategoriaId(categoriaId);
		produto.setDescricao(descricao);
		produto.setImagem(imagem);
		produto.setNome(nome);
		produto.setPreco(preco);
		
		return produto;
	}
	
	private Response adicionarProduto(ProdutoInput input) {
		return given().contentType(MediaType.APPLICATION_JSON_VALUE).body(input)
				.post(ENDPOINT_PRODUTOS);
	}
	
	@Quando("Solicitar a lista de produtos cadastrados na plataforma")
	public void listarProdutos() {
		response = given()
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.when()
					.get(ENDPOINT_PRODUTOS);
	}
	
	@Então("a lista de produtos é retornada com sucesso")
	public void listaProdutosComSucesso() {
		response.then().statusCode(HttpStatus.OK.value());
	}
	
	
	@Dado("um código de categoria para consulta")
	public void adicionarProdutoERegistrarIdCategoria() {
		idCategoria = 1L;
		
		ProdutoInput produto = createProdutoInput("Cheeseburger com duplo de carne e queijo", "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"), idCategoria);
		response = adicionarProduto(produto);
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idProduto = Long.valueOf(idValue);
	}
	
	@Quando("solicitar a lista de produtos da categoria")
	public void listarProdutosPorCodigoCategoria() {
		response = given()
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.pathParam("id", idCategoria)
					.when()
					.get(ENDPOINT_PRODUTOS + "/categorias/codigo/{id}");
	}
	
	@Então("a lista de produtos por código é retornada com sucesso")
	public void asdsa() {
		response.then().statusCode(HttpStatus.OK.value());
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idProduto)
				.when()
				.delete(ENDPOINT_PRODUTOS + "/{id}");
	}
	
	@Dado("um nome de categoria para consulta")
	public void adicionarProdutoERegistrarNomeCategoria() {
		nomeCategoria = "Lanche";
		idCategoria = 1L;
		
		ProdutoInput produto = createProdutoInput("Cheeseburger de carne e queijo", "/cheeseburger.png", "Cheeseburger", new BigDecimal("20.99"), idCategoria);
		response = adicionarProduto(produto);
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idProduto = Long.valueOf(idValue);
	}
	
	@Quando("solicitar a lista de produtos da categoria por nome")
	public void listarProdutosPorNomeCategoria() {
		response = given()
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.pathParam("nome", nomeCategoria)
					.when()
					.get(ENDPOINT_PRODUTOS + "/categorias/nome/{nome}");
	}
	
	@Então("a lista de produtos por nome de categoria é retornada com sucesso")
	public void listaProdutosComSucesso_porNomeCategoria() {
		response.then().statusCode(HttpStatus.OK.value());
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idProduto)
				.when()
				.delete(ENDPOINT_PRODUTOS + "/{id}");
		
	}
	
	@Dado("um código de categoria")
	public void registrarCodigoCategoriaParaInclusaoProduto() {
		idCategoria = 1L;
	}

	@Quando("adicionar produtos na categoria selecionada")
	public void adicionarProdutoCodigoCategoria() {
		ProdutoInput input = createProdutoInput("Hamburger com duplo de carne", "/hamburger.png", "Hamburger duplo", new BigDecimal("15.99"), idCategoria);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(input)
				.when()
				.post(ENDPOINT_PRODUTOS);
	}

	@Então("novo produto deve ser adicionado com sucesso")
	public void produtoAdicionadoComSucesso() {
		response.then().statusCode(HttpStatus.CREATED.value());
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idProduto = Long.valueOf(idValue);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idProduto)
				.when()
				.delete(ENDPOINT_PRODUTOS + "/{id}");
	}
	
	@Dado("que um produto esteja cadastrado na plataforma")
	public void registrarIdProduto() {
		nomeCategoria = "Lanche";
		idCategoria = 1L;
		
		ProdutoInput produto = createProdutoInput("Cheesebacon duplo", "/cheesebacon.png", "Cheesebacon", new BigDecimal("25.99"), idCategoria);
		response = adicionarProduto(produto);
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idProduto = Long.valueOf(idValue);
	}

	@Quando("for solicitado a remoção do produto")
	public void removerProduto() {
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idProduto)
				.when()
				.delete(ENDPOINT_PRODUTOS + "/{id}");
	}

	@Então("o produto deve ser removido da plataforma")
	public void produtoRemovidoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Dado("que um produto esteja cadastrado na plataforma para alteração")
	public void registarIdProdutoParaAlteracao() {
		idCategoria = 4L;
		
		ProdutoInput input = createProdutoInput("Hamburger monster", "/hamburger_monster.png", "Hamburger duplo monster", new BigDecimal("35.99"), idCategoria);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(input)
				.when()
				.post(ENDPOINT_PRODUTOS);
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		
		idProduto = Long.valueOf(idValue);
	}

	@Quando("atualizar o produto na plataforma")
	public void atualizarProdutoNaPlataforma() {
		ProdutoInput input = createProdutoInput("Casquinha sorvete baunilha", "/sorvete_baunilha.png", "Sorvete baunilha", new BigDecimal("5.99"), idCategoria);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idProduto)
				.body(input)
				.when()
				.put(ENDPOINT_PRODUTOS + "/{id}");
	}

	@Então("produto deve ser atualizado com sucesso")
	public void produtoAtualizadoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idProduto)
				.when()
				.delete(ENDPOINT_PRODUTOS + "/{id}");
	}
}
