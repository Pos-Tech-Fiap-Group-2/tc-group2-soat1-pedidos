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
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;

import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;

public class PedidoBDDPassos {

	
	private static String ENDPOINT_CLIENTES;
	
	private static String ENDPOINT_PRODUTOS;
	
	private static String ENDPOINT_CHECKOUT;
	
	private static String ENDPOINT_PEDIDOS;
	
	private Response response;
	
	private Long idCategoria;
	
	private Long idProduto;
	
	private Long idCliente;
	
	private Long idPedido;
	
	private Long idItemPedido;
	
	static {
		Properties prop = new Properties();
		InputStream is = CategoriaBDDPassos.class.getResourceAsStream("/bdd-config.properties");
		
		try {
			prop.load(is);
			ENDPOINT_CLIENTES = prop.getProperty("bdd.endpoint.clientes.url");
			ENDPOINT_PRODUTOS = prop.getProperty("bdd.endpoint.produtos.url");
			ENDPOINT_CHECKOUT = prop.getProperty("bdd.endpoint.checkout.url");
			ENDPOINT_PEDIDOS = prop.getProperty("bdd.endpoint.pedidos.url");
			
		} catch (IOException e) {
			
		}
	}
	
	private Long randomCpf() {
		long leftLimit = 1000000000L;
	    long rightLimit = 9999999999L;
	    long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
	    
	    return generatedLong;
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
	
	private void adicionarCliente(ClienteInput input) {
		response = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(input)
				.post(ENDPOINT_CLIENTES);
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idCliente = Long.valueOf(idValue);
	}
	
	private void adicionarProduto(ProdutoInput input) {
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(input)
				.when()
				.post(ENDPOINT_PRODUTOS);
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idProduto = Long.valueOf(idValue);
	}
	
	private void efetuarCheckout() {
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
		
		String idValue = response.getBody().jsonPath().getJsonObject("id").toString();
		idPedido = Long.valueOf(idValue);
		
		String idValueItem = response.getBody().jsonPath().getJsonObject("itens[0].id").toString();
		idItemPedido = Long.valueOf(idValueItem);
		
		System.out.println("ID PEDIDO: " + idPedido);
	}
	
	@Quando("solicitar a lista de pedidos cadastrados na plataforma")
	public void listarPedidos() {
		idCategoria = 1L;
		long cpf = randomCpf();
		
		ClienteInput clienteInput = createClienteInput(cpf, "cliente-pedidos."+cpf+"@teste.com.br", "Cliente Teste Pedidos "+cpf);
		ProdutoInput produtoInput = createProdutoInput("Produto Teste Pedido 01", "/teste_pedido.png", "Produto Teste Pedido 01", new BigDecimal("25.99"), idCategoria);
		
		this.adicionarCliente(clienteInput);
		this.adicionarProduto(produtoInput);
		this.efetuarCheckout();
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when()
				.get(ENDPOINT_PEDIDOS);
	}

	@Então("a lista de pedidos deve ser apresentada")
	public void listaDePedidosComSucesso() {
		response.then().statusCode(HttpStatus.OK.value());
	}
	
	@Quando("solicitar a lista de pedidos cadastrados na plataforma por status")
	public void listarPedidosComStatus() {
		idCategoria = 1L;
		long cpf = randomCpf();
		
		ClienteInput clienteInput = createClienteInput(cpf, "cliente-pedidos."+cpf+"@teste.com.br", "Cliente Teste Pedidos "+cpf);
		ProdutoInput produtoInput = createProdutoInput("Produto Teste Pedido 02", "/teste_pedido.png", "Produto Teste Pedido 02", new BigDecimal("25.99"), idCategoria);
		
		this.adicionarCliente(clienteInput);
		this.adicionarProduto(produtoInput);
		this.efetuarCheckout();
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.queryParam("status", StatusPedido.RECEBIDO.name())
				.when()
				.get(ENDPOINT_PEDIDOS);
	}

	@Então("a lista de pedidos deve ser apresentada pelo status informado")
	public void listaDePedidosPorStatusComSucesso() {
		response.then().statusCode(HttpStatus.OK.value());
	}
	
	@Quando("consultar o pedido pelo id")
	public void consultarPedidoPeloId() {
		idCategoria = 1L;
		long cpf = randomCpf();
		
		ClienteInput clienteInput = createClienteInput(cpf, "cliente-pedidos."+cpf+"@teste.com.br", "Cliente Teste Pedidos "+cpf);
		ProdutoInput produtoInput = createProdutoInput("Produto Teste Pedido 03", "/teste_pedido.png", "Produto Teste Pedido 03", new BigDecimal("25.99"), idCategoria);
		
		this.adicionarCliente(clienteInput);
		this.adicionarProduto(produtoInput);
		this.efetuarCheckout();		
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idPedido)
				.when()
				.get(ENDPOINT_PEDIDOS + "/{id}");
	}

	@Então("dados do pedido devem ser apresentados")
	public void consultaPedidoPeloIdComSucesso() {
		response.then().statusCode(HttpStatus.OK.value());
	}
	
	@Quando("atualizar status do pedido na plataforma")
	public void atualizarStatusDoPedido() {
		idCategoria = 1L;
		long cpf = randomCpf();
		
		ClienteInput clienteInput = createClienteInput(cpf, "cliente-pedidos."+cpf+"@teste.com.br", "Cliente Teste Pedidos "+cpf);
		ProdutoInput produtoInput = createProdutoInput("Produto Teste Pedido 04", "/teste_pedido.png", "Produto Teste Pedido 04", new BigDecimal("25.99"), idCategoria);
		
		this.adicionarCliente(clienteInput);
		this.adicionarProduto(produtoInput);
		this.efetuarCheckout();
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idPedido)
				.body(StatusPedido.PRONTO)
				.when()
				.patch(ENDPOINT_PEDIDOS + "/{id}/status");
	}

	@Então("pedido com o status atualizado")
	public void consultaPedidoComStatusAlteradoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Quando("adicionar itens ao pedido")
	public void adicionarItensAoPedido() {
		idCategoria = 1L;
		long cpf = randomCpf();
		
		ClienteInput clienteInput = createClienteInput(cpf, "cliente-pedidos."+cpf+"@teste.com.br", "Cliente Teste Pedidos "+cpf);
		ProdutoInput produtoInput = createProdutoInput("Produto Teste Pedido 05", "/teste_pedido.png", "Produto Teste Pedido 05", new BigDecimal("25.99"), idCategoria);
		
		this.adicionarCliente(clienteInput);
		this.adicionarProduto(produtoInput);
		this.efetuarCheckout();
		
		produtoInput = createProdutoInput("Produto Teste Pedido 06", "/teste_pedido.png", "Produto Teste Pedido 06", new BigDecimal("25.99"), idCategoria);
		this.adicionarProduto(produtoInput);
		
		CheckoutItemStub itemStub = new CheckoutItemStub(2, idProduto);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idPedido)
				.body(itemStub)
				.when()
				.post(ENDPOINT_PEDIDOS + "/{id}/items");
	}
	
	@Então("itens adicionados com sucesso ao pedido")
	public void itensAdicionadosComSucesso() {
		response.then().statusCode(HttpStatus.OK.value());
	}
	
	@Quando("atualizar itens ao pedido")
	public void atualizarItensAoPedido() {
		idCategoria = 1L;
		long cpf = randomCpf();
		
		ClienteInput clienteInput = createClienteInput(cpf, "cliente-pedidos."+cpf+"@teste.com.br", "Cliente Teste Pedidos "+cpf);
		ProdutoInput produtoInput = createProdutoInput("Produto Teste Pedido 07", "/teste_pedido.png", "Produto Teste Pedido 07", new BigDecimal("25.99"), idCategoria);
		
		this.adicionarCliente(clienteInput);
		this.adicionarProduto(produtoInput);
		this.efetuarCheckout();
		
		System.out.println("ID PEDIDO: " + idPedido);
		System.out.println("ID PRODUTO: " + idProduto);
		
		CheckoutItemStub itemStubAtualizar = new CheckoutItemStub(2, idProduto, idItemPedido);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idPedido)
				.body(itemStubAtualizar)
				.when()
				.put(ENDPOINT_PEDIDOS + "/{id}/items");
	}
	
	@Então("itens atualizados com sucesso ao pedido")
	public void itensAtualizadosComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	@Quando("remover item do pedido")
	public void removerItemPedido() {
		idCategoria = 1L;
		long cpf = randomCpf();
		
		ClienteInput clienteInput = createClienteInput(cpf, "cliente-pedidos."+cpf+"@teste.com.br", "Cliente Teste Pedidos "+cpf);
		ProdutoInput produtoInput = createProdutoInput("Produto Teste Pedido 08", "/teste_pedido.png", "Produto Teste Pedido 08", new BigDecimal("25.99"), idCategoria);
		
		this.adicionarCliente(clienteInput);
		this.adicionarProduto(produtoInput);
		this.efetuarCheckout();
		
		RemocaoItemPedidoStub itemStub = new RemocaoItemPedidoStub(idProduto);
		
		response = given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", idPedido)
				.body(itemStub)
				.when()
				.delete(ENDPOINT_PEDIDOS + "/{id}/items");
	}
	
	@Então("item removido com sucesso")
	public void itemRemovidoComSucesso() {
		response.then().statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	class RemocaoItemPedidoStub {
		private Long produtoId;
		
		public RemocaoItemPedidoStub(Long produtoId) {
			this.produtoId = produtoId;
		}
		
		public Long getProdutoId() {
			return this.produtoId;
		}
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
		
		public CheckoutItemStub(int quantidade, Long produtoId, Long itemPedidoId) {
			this.itemPedidoId = itemPedidoId;
			this.quantidade = quantidade;
			this.produtoId = produtoId;
		}
		
		private Long itemPedidoId;
		private int quantidade;
		private Long produtoId;
		
		public int getQuantidade() {
			return quantidade;
		}
		public Long getProdutoId() {
			return produtoId;
		}
		public Long getItemPedidoId() {
			return itemPedidoId;
		}
	}
}
