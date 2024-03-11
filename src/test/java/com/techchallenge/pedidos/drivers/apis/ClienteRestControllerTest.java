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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.Locale;

import com.techchallenge.pedidos.core.domain.entities.Endereco;
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
import org.springframework.validation.BindException;

import com.techchallenge.pedidos.adapter.controllers.ClienteController;
import com.techchallenge.pedidos.adapter.driver.exceptionhandler.ApiExceptionHandler;
import com.techchallenge.pedidos.adapter.driver.exceptionhandler.ValidacaoException;
import com.techchallenge.pedidos.adapter.driver.model.ClienteModel;
import com.techchallenge.pedidos.adapter.driver.model.input.ClienteAtualizacaoInput;
import com.techchallenge.pedidos.adapter.driver.model.input.ClienteInput;
import com.techchallenge.pedidos.core.domain.entities.Cliente;
import com.techchallenge.pedidos.utils.ResourceUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class ClienteRestControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private ClienteRestController rest;
	
	@Mock
	private ClienteController controller;
	
	private Cliente createCliente(Long id, Long cpf, String email, String nome, Long telefone, Boolean ativo, Endereco endereco) {
		Cliente cliente = new Cliente();
		
		cliente.setId(id);
		cliente.setCpf(cpf);
		cliente.setNome(nome);
		cliente.setTelefone(telefone);
		cliente.setEmail(email);
		cliente.setAtivo(ativo);
		cliente.setEndereco(endereco);

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
		
		input.setEmail(cliente.getEmail());
		input.setNome(cliente.getNome());
		input.setTelefone(cliente.getTelefone());
		input.setAtivo(cliente.getAtivo());
		input.setEndereco(cliente.getEndereco());
		
		return input;
	}
	
	private ClienteModel createClienteModel(Cliente cliente) {
		ClienteModel model = new ClienteModel();
		
		model.setCpf(cliente.getCpf());
		model.setEmail(cliente.getEmail());
		model.setId(cliente.getId());
		model.setNome(cliente.getNome());
		model.setTelefone(cliente.getTelefone());
		model.setAtivo(cliente.getAtivo());
		model.setEndereco(cliente.getEndereco());
		
		return model;
	}
	
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
	public void adicionar() throws Exception {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 964527898L, Boolean.TRUE, new Endereco());
    	ClienteInput input = createClienteInput(cliente);
    	ClienteModel model = createClienteModel(cliente);
    	
		when(controller.adicionar(input)).thenReturn(model);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/cliente-input.json");
		
		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(content))
			.andExpect(status().isCreated());
		
		verify(controller, times(1)).adicionar(any(ClienteInput.class));
	}
	
	@Test
	public void buscarPorCpf() throws Exception {
    	Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 964527898L, Boolean.TRUE, new Endereco());
    	ClienteModel model = createClienteModel(cliente);
    	
		when(controller.buscarPorCpf(12345678901L)).thenReturn(model);
		
		mockMvc.perform(get("/clientes/12345678901")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isOk())
			      .andExpect(jsonPath("$.cpf").value(model.getCpf()))
			      .andExpect(jsonPath("$.email").value(model.getEmail()))
			      .andExpect(jsonPath("$.id").value(model.getId()))
			      .andExpect(jsonPath("$.nome").value(model.getNome()));
				
		verify(controller, times(1)).buscarPorCpf(any(Long.class));
	}
	
	@Test
	public void remover() throws Exception {
		
		doNothing().when(controller).remover(12345678901L);
		
		mockMvc.perform(delete("/clientes/12345678901")
			      .contentType(MediaType.APPLICATION_JSON))
			      .andExpect(status().isNoContent());
		
		verify(controller, times(1)).remover(any(Long.class));
	}
	
	@Test
    public void atualizarDadosCliente() throws Exception {
		
		Cliente cliente = createCliente(1L, 12345678901L, "cliente.teste@teste.com.br", "Cliente Teste", 964527898L, Boolean.TRUE, new Endereco());
		ClienteAtualizacaoInput input = createClienteAtualizacaoInput(cliente);
		
		doNothing().when(controller).atualizarDadosCliente(cliente.getId(), input);
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/cliente-atualizacao-input.json");
		
		mockMvc.perform(patch("/clientes/1")
			      .contentType(MediaType.APPLICATION_JSON).content(content))
			      .andExpect(status().isNoContent());
		
		verify(controller, times(1)).atualizarDadosCliente(any(Long.class), any(ClienteAtualizacaoInput.class));
    }
	
	@Test
	public void dadoInclusaoCliente_quandoPayloadIncompleto_entaoStatus400() throws Exception {
		
		String content = ResourceUtil.getContentFromResource(
				"/json/incorreto/cliente-input-sem-nome.json");

		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void dadoInclusaoCliente_quandoConteudoInvalido_entaoStatus400() throws Exception {
		
		String content = ResourceUtil.getContentFromResource(
				"/json/incorreto/cliente-input-cpf-conteudo-invalido");

		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content(content))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void dadoInclusaoCliente_quandoEstiverComMediaTypeIncorreto_entaoStatus400() throws Exception {
		
		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_XML).content(""))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void dadoInclusaoCliente_quandoEstiverConteudoInvalido_entaoStatus400() throws Exception {
		
		mockMvc.perform(post("/clientes").contentType(MediaType.APPLICATION_JSON).content("<xml><teste>1</teste></xml>"))
				.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void dadoInclusaoCliente_quandoOcorrerErroInterno_entaoStatus500() throws Exception {
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/cliente-input.json");
		
		when(controller.adicionar(any(ClienteInput.class)))
			.thenThrow(new RuntimeException("Erro inesperado"));
	
		mockMvc.perform(post("/clientes")
	      .contentType(MediaType.APPLICATION_JSON).content(content))
	      .andExpect(status().is5xxServerError());
	}
	
	@Test
	public void dadoInclusaoCliente_quandoOcorrerErroValidacao_entaoStatus4oo() throws Exception {
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/cliente-input.json");
		
		when(controller.adicionar(any(ClienteInput.class))).thenThrow(new ValidacaoException(new BindException(controller, "Test")));
	
		mockMvc.perform(post("/clientes")
	      .contentType(MediaType.APPLICATION_JSON).content(content))
	      .andExpect(status().is4xxClientError());
	}
}