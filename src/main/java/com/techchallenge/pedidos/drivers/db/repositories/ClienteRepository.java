package com.techchallenge.pedidos.drivers.db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techchallenge.pedidos.drivers.db.entities.ClienteEntity;


@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    @SuppressWarnings("unchecked")
	ClienteEntity save(ClienteEntity cliente);

    @Query("select c from Cliente c where c.ativo = true")
    List<ClienteEntity> findByCpfIs(Long cpf);
    
    @Query("select c from Cliente c where c.cpf = :cpf or c.email = :email and c.ativo = true")
    List<ClienteEntity> findByCpfOrEmail(Long cpf, String email);
}