package com.hexagonalexample.meiospagamento.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.hexagonalexample.meiospagamento.domain.model.PagamentoMetric;

@Repository
public interface PagamentoMetricRepository extends CassandraRepository<PagamentoMetric, UUID> {
}