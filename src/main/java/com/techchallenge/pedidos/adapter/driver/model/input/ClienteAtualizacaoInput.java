package com.techchallenge.pedidos.adapter.driver.model.input;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.techchallenge.pedidos.core.domain.entities.Endereco;
import io.swagger.annotations.ApiModelProperty;

public class ClienteAtualizacaoInput {

    @ApiModelProperty(example = "Nome do cliente")
    @JsonProperty(required = true)
    @NotNull
    private String nome;

    @ApiModelProperty(example = "cliente-teste@teste.com.br")
    @JsonProperty(required = true)
    @NotNull
    private String email;

    @ApiModelProperty(example = "964527898")
    private Long telefone;

    @ApiModelProperty(example = "false")
    private Boolean ativo;

    @ApiModelProperty(example = "Rua das Flores, 2345, Centro. CEP: 12345-678, São Paulo - SP")
    private Endereco endereco;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
