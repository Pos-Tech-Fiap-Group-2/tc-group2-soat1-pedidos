package com.techchallenge.pedidos.drivers.apis;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.techchallenge.pedidos.adapter.controllers.CategoriaController;
import com.techchallenge.pedidos.adapter.driver.exceptionhandler.ApiExceptionHandler;
import com.techchallenge.pedidos.adapter.driver.model.CategoriaModel;

@RunWith(SpringJUnit4ClassRunner.class)
public class CategoriaRestControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private CategoriaRestController rest;
	
    @Mock
    private CategoriaController controller;
    
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
	
    @SuppressWarnings("serial")
	private List<CategoriaModel> createCategoriaModels() {
    	
    	return new ArrayList<CategoriaModel>() {{
    		this.add(createCategoriaModel(1L, "Lanche"));
    		this.add(createCategoriaModel(2L, "Bebida"));
    		this.add(createCategoriaModel(3L, "Acompanhamento"));
    		this.add(createCategoriaModel(4L, "Sobremesa"));
    	}};
    }

	private CategoriaModel createCategoriaModel(Long id, String nome) {
		CategoriaModel categoria = new CategoriaModel();
		
    	categoria.setId(id);
    	categoria.setNome(nome);
    	
    	return categoria;
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
	public void dado_listarCategorias_entao_retorna200() throws Exception {

		when(controller.listar()).thenReturn(createCategoriaModels());
		
		mockMvc.perform(get("/api/categorias").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
