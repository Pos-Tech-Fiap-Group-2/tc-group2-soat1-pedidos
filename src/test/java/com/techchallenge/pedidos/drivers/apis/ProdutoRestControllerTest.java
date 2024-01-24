package com.techchallenge.pedidos.drivers.apis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.techchallenge.pedidos.adapter.controllers.ProdutoController;
import com.techchallenge.pedidos.adapter.driver.exceptionhandler.ApiExceptionHandler;
import com.techchallenge.pedidos.adapter.driver.model.CategoriaResumoModel;
import com.techchallenge.pedidos.adapter.driver.model.ProdutoModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ProdutoInput;
import com.techchallenge.pedidos.core.domain.entities.Categoria;
import com.techchallenge.pedidos.core.domain.entities.Produto;
import com.techchallenge.pedidos.core.domain.exception.EntidadeNaoEncontradaException;
import com.techchallenge.pedidos.core.domain.exception.NegocioException;
import com.techchallenge.pedidos.utils.ResourceUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProdutoRestControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private ProdutoRestController rest;
	
    @Mock
    private ProdutoController controller;
    
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
	
	private ProdutoInput createProdutoInput(Produto produto) {
		ProdutoInput input = new ProdutoInput();
		
		input.setCategoriaId(produto.getCategoria().getId());
		input.setDescricao(produto.getDescricao());
		input.setImagem(produto.getImagem());
		input.setNome(produto.getNome());
		input.setPreco(produto.getPreco());
		
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
    public void listar() throws Exception {
    	
		mockMvc.perform(get("/produtos")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
				
		verify(controller, times(1)).listar();
    }

    @Test
    public void listarPorCategoria_por_nome() throws Exception {
    	
    	List<Produto> produtos = new ArrayList<>();
    	
    	produtos.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	produtos.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<ProdutoModel> models = produtos.stream().map(p -> createProdutoModel(p)).collect(Collectors.toList());
    	
    	when(controller.listarPorCategoria("Lanche")).thenReturn(models);
    	
		mockMvc.perform(get("/produtos/categorias/nome/Lanche")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
		
		verify(controller, times(1)).listarPorCategoria(any(String.class));
    }
    
    @Test
    public void listarPorCategoria_por_id() throws Exception {
    	
    	List<Produto> produtos = new ArrayList<>();
    	
    	produtos.add(createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99")));
    	produtos.add(createProduto("Hamburger com duplo de carne", 2L, "/hamburger.png", "Hamburger", new BigDecimal("17.99")));
    	
    	List<ProdutoModel> models = produtos.stream().map(p -> createProdutoModel(p)).collect(Collectors.toList());
    	
    	when(controller.listarPorCategoria(1L)).thenReturn(models);
    	
		mockMvc.perform(get("/produtos/categorias/codigo/1")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk());
		
		verify(controller, times(1)).listarPorCategoria(any(Long.class));
    }
    
    @Test
	public void adicionar() throws Exception {
		
    	Produto produto = createProduto("Cheeseburger com duplo de carne e queijo", 1L, "/cheeseburger.png", "Cheeseburger duplo", new BigDecimal("20.99"));
    	ProdutoModel model = createProdutoModel(produto);
    	ProdutoInput input = createProdutoInput(produto);
    	
		when(controller.adicionar(input)).thenReturn(model);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/produto-input.json");
		
		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(content))
			.andExpect(status().isCreated());

		verify(controller, times(1)).adicionar(any(ProdutoInput.class));
	}

    @Test
	public void remover() throws Exception {
		
		doNothing().when(controller).remover(1L);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/item-pedido-exclusao-input.json");
		
		mockMvc.perform(delete("/produtos/1")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().isNoContent());
		
		verify(controller, times(1)).remover(any(Long.class));
	}
	
    @Test
	public void atualizar() throws Exception {

    	Produto produto = createProduto("Sorvete casquinha Baunilha", 1L, "/imgs/products/icecream2.png", "Sorvete casquinha Baunilha", new BigDecimal("2.50"));
    	ProdutoInput input = createProdutoInput(produto);
    	
    	doNothing().when(controller).atualizar(produto.getId(), input);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/produto-atualizacao-input.json");
		
		mockMvc.perform(put("/produtos/1")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().isNoContent());
		
		verify(controller, times(1)).atualizar(any(Long.class), any(ProdutoInput.class));
	}
    
	@Test
	public void dadoExclusaoProduto_quandoNaoEncontrarComIdInformado_entaoStatus404() throws Exception {
		
		doThrow(EntidadeNaoEncontradaException.class).when(controller).remover(1L);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/item-pedido-exclusao-input.json");
		
		mockMvc.perform(delete("/produtos/1")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().is4xxClientError());
	}
	
	@Test
	public void dadoExclusaoProduto_quandoNaoEncontrarComIdInformado_entaoStatus400() throws Exception {
		
		doThrow(NegocioException.class).when(controller).remover(1L);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/item-pedido-exclusao-input.json");
		
		mockMvc.perform(delete("/produtos/1")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().is4xxClientError());
	}
}
