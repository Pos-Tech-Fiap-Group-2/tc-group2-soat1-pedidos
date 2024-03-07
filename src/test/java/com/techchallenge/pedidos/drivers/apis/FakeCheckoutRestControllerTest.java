package com.techchallenge.pedidos.drivers.apis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.lang.reflect.Field;
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

import com.techchallenge.pedidos.adapter.controllers.FakeCheckoutController;
import com.techchallenge.pedidos.adapter.driver.exceptionhandler.ApiExceptionHandler;
import com.techchallenge.pedidos.adapter.driver.model.input.PedidoInput;
import com.techchallenge.pedidos.utils.ResourceUtil;

@RunWith(SpringJUnit4ClassRunner.class)
public class FakeCheckoutRestControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private FakeCheckoutRestController rest;
	
	@Mock
	private FakeCheckoutController controller;
	
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
	public void checkout() throws Exception {
		
		String content = ResourceUtil.getContentFromResource(
				"/json/correto/fake-checkout-input.json");
		
		mockMvc.perform(post("/api/checkout").contentType(MediaType.APPLICATION_JSON).content(content))
			.andExpect(status().isOk());
		
		verify(controller, times(1)).checkout(any(PedidoInput.class));
	}
}
