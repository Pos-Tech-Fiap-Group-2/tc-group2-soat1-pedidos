package com.techchallenge.pedidos.core.domain.Interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "producao", url = "${url.microservico.producao}")
@RequestMapping("/api/producao")
public interface ProducaoClient {

    @PostMapping("/{id}/adicionar")
    void adicionarProducao(@RequestParam("id") Long id);
}
