package com.techchallenge.pedidos.adapter.driver.model;

import com.techchallenge.pedidos.core.domain.entities.Endereco;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Cliente")
public class ClienteModel {

	@ApiModelProperty(example = "ID do cliente")
	private Long id;
	@ApiModelProperty(example = "Nome do cliente")
	private String nome;
	@ApiModelProperty(example = "cliente-teste@teste.com.br")
	private String email;
	@ApiModelProperty(example = "952486532")
	private Long telefone;
	@ApiModelProperty(example = "Endereço do cliente")
	private Endereco endereco;
	@ApiModelProperty(example = "12345678901")
	private Long cpf;
    @ApiModelProperty(example = "true")
	private Boolean ativo;

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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
