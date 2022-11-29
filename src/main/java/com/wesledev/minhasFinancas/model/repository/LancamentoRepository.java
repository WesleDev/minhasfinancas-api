package com.wesledev.minhasFinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wesledev.minhasFinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
