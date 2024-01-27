package com.techchallenge.pedidos.bdd;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.techchallenge.pedidos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pedidos.adapter.driver.model.input.ProdutoInput;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class CheckoutBDDPassos {

	private Response response;
	
	private Long idCategoria;
	
	private Long idProduto;
	
	private Long idCliente;

	private static String ENDPOINT_CLIENTES;
	private static String ENDPOINT_PRODUTOS;
	private static String ENDPOINT_CHECKOUT;
	
	static {
		Properties prop = new Properties();
		InputStream is = CategoriaBDDPassos.class.getResourceAsStream("/bdd-config.properties");
		
		try {
			prop.load(is);
			ENDPOINT_CLIENTES = prop.getProperty("bdd.endpoint.clientes.url");
			ENDPOINT_PRODUTOS = prop.getProperty("bdd.endpoint.produtos.url");
			ENDPOINT_CHECKOUT = prop.getProperty("bdd.endpoint.checkout.url");
			
		} catch (IOException e) {
			
		}
	}
	
	private ClienteInput createClienteInput(Long cpf, String email, String nome) {
		ClienteInput input = new ClienteInput();
		
		input.setCpf(cpf);
		input.setEmail(email);
		input.setNome(nome);
		
		return input;
	}
	
	private ProdutoInput createProdutoInput(String descricao, String imagem, String nome, BigDecimal preco, Long categoriaId) {
		ProdutoInput produto = new ProdutoInput();
		
		produto.setCategoriaId(categoriaId);
		produto.setDescricao(descricao);
		produto.setImagem(imagem);
		produto.setNome(nome);
		produto.setPreco(preco);
		
		return produto;
	}
	
	private Long randomCpf() {
		long leftLimit = 1000000000L;
	    long rightLimit = 9999999999L;
	    long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
	    
	    return generatedLong;
	}
	
	private void adicionarCliente() {
		
		Long cpf = randomCpf();
		ClienteInput input = createClienteInput(cpf, "cliente-checkout" + cpf + ".teste@teste.com.br", "Cliente Teste Checkout " + cpf);
		
		response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(input)
				.post(ENDPOINT_CLIENTES);
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idCliente = Long.valueOf(idValue);
	}
	
	private void adicionarProduto() {
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
	
	@Dado("um produto e quantidade de itens e id do cliente")
	public void consultarClienteNaPlataforma() {
		this.adicionarCliente();
		this.adicionarProduto();
	}

	@Quando("efetuar o checkout do pedido na plataforma")
	public void efetuarCheckout() {
		@SuppressWarnings("serial")
		List<CheckoutItemStub> itens = new ArrayList<>() {{
			this.add(new CheckoutItemStub(1, idProduto));
		}};
		
		CheckoutStub checkout = new CheckoutStub(itens, idCliente);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(checkout)
				.when()
				.post(ENDPOINT_CHECKOUT);
	}

	@Então("checkout realizado com sucesso")
	public void checkoutRealizadoComSucesso() {
		response.then().statusCode(HttpStatus.OK.value());
	}
	
	class CheckoutStub {
		
		public CheckoutStub(List<CheckoutItemStub> itens, Long clienteId) {
			this.itens = itens;
			this.clienteId = clienteId;
		}
		
		private List<CheckoutItemStub> itens = new ArrayList<>();
		private Long clienteId;
		
		public List<CheckoutItemStub> getItens() {
			return itens;
		}
		public Long getClienteId() {
			return clienteId;
		}
	}
	
	class CheckoutItemStub {
		
		public CheckoutItemStub(int quantidade, Long produtoId) {
			this.quantidade = quantidade;
			this.produtoId = produtoId;
		}
		
		private int quantidade;
		private Long produtoId;
		
		public int getQuantidade() {
			return quantidade;
		}
		public Long getProdutoId() {
			return produtoId;
		}
	}
}