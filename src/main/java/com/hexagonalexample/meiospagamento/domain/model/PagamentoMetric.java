package com.hexagonalexample.meiospagamento.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("pagamentos_metricas")
public class PagamentoMetric {

	@Id
	@PrimaryKey
	private UUID id;
	private String tipoPagamento;
	private double valor;
	private String status;
	private Instant timestamp;

	public PagamentoMetric() {
		this.id = UUID.randomUUID();
		this.timestamp = Instant.now();
	}

	public UUID getId() {
		return id;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Instant getTimestamp() {
		return timestamp;
	}
}
