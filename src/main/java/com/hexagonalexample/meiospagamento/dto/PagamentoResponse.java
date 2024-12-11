package com.hexagonalexample.meiospagamento.dto;

public class PagamentoResponse {

	private String id;
	private String status; // "PEDENTE", "APROVADO", "FALHA"
	private String mensagem;

	public PagamentoResponse() {
	}

	public PagamentoResponse(Object object, String string, String message) {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		return "PagamentoResponse [id=" + id + ", status=" + status + ", mensagem=" + mensagem + "]";
	}

}
