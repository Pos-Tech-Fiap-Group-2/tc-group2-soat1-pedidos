package com.techchallenge.pedidos.drivers.apis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.techchallenge.pedidos.adapter.controllers.PedidoController;
import com.techchallenge.pedidos.adapter.driver.exceptionhandler.ApiExceptionHandler;
import com.techchallenge.pedidos.adapter.driver.model.CategoriaResumoModel;
import com.techchallenge.pedidos.adapter.driver.model.ClienteModel;
import com.techchallenge.pedidos.adapter.driver.model.ItemPedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.PedidoModel;
import com.techchallenge.pedidos.adapter.driver.model.ProdutoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ItemPedidoInput;
import com.techchallenge.pedidos.adapter.driver.model.input.StatusPedidoInput;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.core.domain.entities.ItemPedido;
import com.techchallenge.pedidos.core.domain.entities.Pedido;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;
import com.techchallenge.pedidos.utils.ResourceUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class PedidoRestControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private PedidoRestController rest;
	
    @Mock
    private PedidoController controller;
    
	private ApiExceptionHandler createHandler() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		// Geração de instância de MessageSource mock para injeção no Handler via Reflection API.
		MessageSource messageSourceTest = new MessageSource() {

			public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
				return String.format("Message mock: Code: %s - DefaultMessage: %s : args %s", new Object[] {code, defaultMessage, args});
			}

			public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
				return String.format("Message mock: Code: %s : args %s", new Object[] {code, args});
			}

			public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
				return "Message mock";
			}
		};

		// Como não estamos subindo o contexto do Spring boot, o MessageSource fica com referência nula
		// por estarmos instanciando diretamente o Handler.
		ApiExceptionHandler handler = new ApiExceptionHandler();
		
		// Injeção da referência do messageSource via reflection API.
		Class<? extends ApiExceptionHandler> clazz = handler.getClass();
		
		Field messageSourceField = clazz.getDeclaredField("messageSource");
		messageSourceField.setAccessible(Boolean.TRUE);
		
		messageSourceField.set(handler, messageSourceTest);
		
		return handler;
	}
	
	private Cliente createCliente(Long id, Long cpf, String email, String nome) {
		Cliente cliente = new Cliente();
		
		cliente.setCpf(cpf);
		cliente.setEmail(email);
		cliente.setId(id);
		cliente.setNome(nome);
		
		return cliente;
	}
	
	private Categoria createCategoria(Long id, String nome) {
		Categoria categoria = new Categoria();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
	}
	
	private Produto createProduto(String descricao, Long id, String imagem, String nome, BigDecimal preco) {
		Produto produto = new Produto();
		
		produto.setCategoria(createCategoria(1L, "Lanche"));
		produto.setDescricao(descricao);
		produto.setId(id);
		produto.setImagem(imagem);
		produto.setNome(nome);
		produto.setPreco(preco);
		
		return produto;
	}
	
    private Pedido createPedido(Long id, Cliente cliente) {
    	return createPedido(id, cliente, StatusPedido.RECEBIDO);
    }
    
    private Pedido createPedido(Long id, Cliente cliente, StatusPedido status) {
    	Pedido pedido = new Pedido();
    	
    	pedido.setCliente(cliente);
    	pedido.setId(id);
    	pedido.setItens(createItensPedido(pedido));
    	pedido.setStatus(status);
    	
    	return pedido;
    }
    
	private ItemPedido createItemPedido(Long id, Pedido pedido, BigDecimal precoTotal, Produto produto, Integer quantidade) {
		ItemPedido itemPedido = new ItemPedido();
		
		itemPedido.setId(id);
		itemPedido.setPedido(pedido);
		itemPedido.setPrecoTotal(precoTotal);
		itemPedido.setProduto(produto);
		itemPedido.setQuantidade(quantidade);
		
		return itemPedido;
	}
	
	@SuppressWarnings("serial")
	private List<ItemPedido> createItensPedido(Pedido pedido) {
		
		return new ArrayList<ItemPedido>() {{
			this.add(createItemPedido(1L, pedido, new BigDecimal("25.99"), createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99")), 2));
			this.add(createItemPedido(1L, pedido, new BigDecimal("20.99"), createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger duplo", new BigDecimal("20.99")), 1));
		}};
	}
	
    private PedidoModel createPedidoModel(Pedido pedido) {
    	PedidoModel model = new PedidoModel();
    	
    	model.setCliente(createClienteModel(pedido.getCliente()));
    	model.setDataCancelamento(pedido.getDataCancelamento());
    	model.setDataFinalizacao(pedido.getDataFinalizacao());
    	model.setDataSolicitacao(pedido.getDataSolicitacao());
    	model.setId(pedido.getId());
    	model.setItens(createItensPedidoModel(pedido.getItens()));
    	model.setStatus(pedido.getStatus());
    	model.setValor(pedido.getValor());
    	
    	return model;
    }
    
    private List<PedidoModel> createPedidoModels(List<Pedido> pedidos) {
    	return pedidos.stream().map(p -> createPedidoModel(p)).collect(Collectors.toList());
    }
    
	private List<ItemPedidoModel> createItensPedidoModel(List<ItemPedido> itens) {
		return itens.stream().map(i -> createItemPedidoModel(i)).collect(Collectors.toList());
	}
	
	private ItemPedidoModel createItemPedidoModel(ItemPedido itemPedido) {
		ItemPedidoModel model = new ItemPedidoModel();
		
		model.setPrecoTotal(itemPedido.getPrecoTotal());
		model.setProduto(createProdutoModel(itemPedido.getProduto()));
		model.setQuantidade(itemPedido.getQuantidade());
		
		return model;
	}
	
	private ProdutoModel createProdutoModel(Produto produto) {
		ProdutoModel model = new ProdutoModel();
		
		model.setCategoria(createCategoriaResumoModel(produto.getCategoria()));
		model.setDescricao(produto.getDescricao());
		model.setId(produto.getId());
		model.setImagem(produto.getImagem());
		model.setNome(produto.getNome());
		model.setPreco(produto.getPreco());
	
		return model;
	}
	
	private CategoriaResumoModel createCategoriaResumoModel(Categoria categoria) {
		CategoriaResumoModel model = new CategoriaResumoModel();
		
		model.setNome(categoria.getNome());
		
		return model;
	}
	
    private ClienteModel createClienteModel(Cliente cliente) {
    	ClienteModel model = new ClienteModel();
    	
    	model.setCpf(cliente.getCpf());
    	model.setEmail(cliente.getEmail());
    	model.setId(cliente.getId());
    	model.setNome(cliente.getNome());
    	
    	return model;
    }
    
	private ItemPedidoInput createItemPedidoInput(ItemPedido itemPedido) {
		ItemPedidoInput input = new ItemPedidoInput();
		
		input.setProdutoId(itemPedido.getProduto().getId());
		input.setQuantidade(itemPedido.getQuantidade());
		
		return input;
	}
	
	@BeforeEach
	private void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MockitoAnnotations.initMocks(this);
		
		// Handler mock.
		ApiExceptionHandler handler = createHandler();
		
		mockMvc = MockMvcBuilders.standaloneSetup(rest).setControllerAdvice(handler)
				.addFilter((request, response, chain) -> {
					response.setCharacterEncoding("UTF-8");
					chain.doFilter(request, response);
				}, "/*").build();
	}
	
	@Test
    public void buscarPedidosPorId() throws Exception {
    	
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		PedidoModel model = createPedidoModel(pedido);
		
    	when(controller.buscarPedidosPorId(pedido.getId())).thenReturn(model);
    	
		mockMvc.perform(get("/api/pedidos/1")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.id").value(model.getId()))
			      .andExpect(jsonPath("$.cliente.id").value(model.getCliente().getId()))
			      .andExpect(jsonPath("$.cliente.nome").value(model.getCliente().getNome()))
			      .andExpect(jsonPath("$.cliente.cpf").value(model.getCliente().getCpf()))
			      .andExpect(jsonPath("$.cliente.email").value(model.getCliente().getEmail()))
			      .andExpect(jsonPath("$.status").value(model.getStatus().name()))
			      .andExpect(jsonPath("$.valor").value(model.getValor()));
				
		verify(controller, times(1)).buscarPedidosPorId(any(Long.class));
    }

    @Test
    public void listarPedidos_com_parametro() throws Exception {
    	
    	List<Pedido> pedidos = new ArrayList<>();
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		
		pedidos.add(createPedido(1L, cliente));
		pedidos.add(createPedido(2L, cliente));
		
    	List<PedidoModel> models = createPedidoModels(pedidos);
    	
    	when(controller.listarPedidos(StatusPedido.RECEBIDO)).thenReturn(models);
    	
		mockMvc.perform(get("/api/pedidos?status=RECEBIDO")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
		
		verify(controller, times(1)).listarPedidos(any(StatusPedido.class));
    }
    
    @Test
    public void listarPedidos_sem_parametro() throws Exception {
    	
    	List<Pedido> pedidos = new ArrayList<>();
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		
		pedidos.add(createPedido(1L, cliente));
		pedidos.add(createPedido(2L, cliente));
		
    	List<PedidoModel> models = createPedidoModels(pedidos);
    	
    	when(controller.listarPedidos()).thenReturn(models);
    	
		mockMvc.perform(get("/api/pedidos")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
		
		verify(controller, times(1)).listarPedidos();
    }

    @Test
    public void atualizarStatusDoPedido() throws Exception {
    	
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/pedido-status-input.json");
		
		StatusPedidoInput input = new StatusPedidoInput();
		input.setStatus(StatusPedido.PRONTO);
		doNothing().when(controller).atualizarStatusDoPedido(1L, input);
		
		mockMvc.perform(patch("/api/pedidos/1/status")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().isNoContent());
		
		verify(controller, times(1)).atualizarStatusDoPedido(any(Long.class), any(StatusPedidoInput.class));
    }

    @Test
	public void adicionarProduto() throws Exception {
		
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
    	ItemPedidoInput itemPedidoInput = createItemPedidoInput(itemPedido);
    	ItemPedidoModel model = createItemPedidoModel(itemPedido);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/item-pedido-input.json");
		
		when(controller.adicionarProduto(1L, itemPedidoInput)).thenReturn(model);
		
		mockMvc.perform(post("/api/pedidos/1/items").contentType(MediaType.APPLICATION_JSON).content(content))
			.andExpect(status().isOk());
		
		verify(controller, times(1)).adicionarProduto(any(Long.class), any(ItemPedidoInput.class));
	}
	
    @Test
	public void atualizarProduto() throws Exception {
		
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
    	ItemPedidoInput itemPedidoInput = createItemPedidoInput(itemPedido);
    	
    	doNothing().when(controller).atualizarProduto(pedido.getId(), itemPedidoInput);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/item-pedido-input.json");
		
		mockMvc.perform(put("/api/pedidos/1/items")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().isNoContent());
		
		verify(controller, times(1)).atualizarProduto(any(Long.class), any(ItemPedidoInput.class));
	}
	
	@Test
	public void remover() throws Exception {
		
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste");
		Pedido pedido = createPedido(1L, cliente);
		Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("25.99"));
		
		ItemPedido itemPedido = createItemPedido(1L, pedido, new BigDecimal("25.99"), produto, 1);
    	ItemPedidoInput itemPedidoInput = createItemPedidoInput(itemPedido);
		
		doNothing().when(controller).remover(1L, itemPedidoInput);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/item-pedido-exclusao-input.json");
		
		mockMvc.perform(delete("/api/pedidos/1/items")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().isNoContent());
		
		verify(controller, times(1)).remover(any(Long.class), any(ItemPedidoInput.class));
	}
}
