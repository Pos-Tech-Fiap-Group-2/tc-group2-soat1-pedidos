package com.techchallenge.pedidos.adapter.driver.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

//import com.techchallenge.pedidos.core.domain.entities.StatusPagamento;
import com.techchallenge.pedidos.core.domain.entities.StatusPedido;

public class PedidoModel {
	
	private Long id;
    private List<ItemPedidoModel> itens;
    private BigDecimal valor;
    private StatusPedido status;
    private ClienteModel cliente;
    private OffsetDateTime dataSolicitacao;
    private OffsetDateTime dataCancelamento;
    private OffsetDateTime dataFinalizacao;

    public List<ItemPedidoModel> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoModel> itens) {
        this.itens = itens;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public OffsetDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(OffsetDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public OffsetDateTime getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(OffsetDateTime dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

    public OffsetDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(OffsetDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PedidoModel other = (PedidoModel) obj;
		return Objects.equals(id, other.id);
	}
}
