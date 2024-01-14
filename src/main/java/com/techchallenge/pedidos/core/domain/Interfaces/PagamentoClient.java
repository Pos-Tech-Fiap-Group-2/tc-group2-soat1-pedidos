package com.techchallenge.pedidos.core.domain.Interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "pagamento", url = "${url.microservico.pagamento}")
@RequestMapping("/api/pagamento")
public interface PagamentoClient {

    @PostMapping("/{id}/pagamento")
    void enviarPedidoParaPagamento(Long id);
}
