package com.techchallenge.pedidos.drivers.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.techchallenge.pedidos.drivers.db.entities.CategoriaEntity;


@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {

    @SuppressWarnings("unchecked")
	CategoriaEntity save(CategoriaEntity categoria);

    @Query("select c from Categoria c where c.nome = ?1")
    public CategoriaEntity findByNome(String nome);
}
