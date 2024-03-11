package com.techchallenge.pedidos.core.domain.entities;

import java.util.Objects;

public class Cliente {

	private Long id;
    private String nome;
    private String email;
	private Long telefone;
	private Endereco endereco;
    private Long cpf;
	private Boolean ativo = Boolean.TRUE;

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

	public void setTelefone(Long telefone) {this.telefone = telefone;}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
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

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, email, id, nome, telefone, endereco, ativo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Cliente cliente = (Cliente) obj;
		return Objects.equals(id, cliente.id) && Objects.equals(nome, cliente.nome) && Objects.equals(email, cliente.email) && Objects.equals(telefone, cliente.telefone) && Objects.equals(endereco, cliente.endereco) && Objects.equals(cpf, cliente.cpf) && Objects.equals(ativo, cliente.ativo);
	}
}