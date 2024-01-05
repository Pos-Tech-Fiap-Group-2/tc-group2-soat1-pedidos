package com.techchallenge.pedidos.drivers.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techchallenge.pedidos.drivers.db.entities.TipoPagamentoEntity;


@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamentoEntity, Long> {

}
