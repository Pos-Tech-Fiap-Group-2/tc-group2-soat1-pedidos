package com.techchallenge.pedidos.drivers.db.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techchallenge.pedidos.drivers.db.entities.PagamentoEntity;

@Repository
public interface PagamentoRepository extends JpaRepository<PagamentoEntity, Long> {
	
}
