package com.techchallenge.pedidos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({WebConfig.class, SecurityConfiguration.class})
public class TechChallengePedidosApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechChallengePedidosApplication.class, args);
	}

}
