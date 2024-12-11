package com.hexagonalexample.meiospagamento.dto;

public class PagamentoRequest {

	private String tipoPagamento; //"CREDITO", "DEBITO", PIX
	private double valor;
	private String destinatario;

	public PagamentoRequest() {

	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	@Override
	public String toString() {
		return "PagamentoRequest [tipoPagamento=" + tipoPagamento + ", valor=" + valor + ", destinatario="
				+ destinatario + "]";
	}

}
