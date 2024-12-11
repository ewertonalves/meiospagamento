package com.hexagonalexample.meiospagamento.domain.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

import com.hexagonalexample.meiospagamento.dto.PagamentoRequest;
import com.hexagonalexample.meiospagamento.dto.PagamentoResponse;
import com.hexagonalexample.meiospagamento.infrastructure.feign.PagamentoSecundarioClient;

@Service
public class PagamentoService {

	private static final Logger logger = LoggerFactory.getLogger(PagamentoService.class);
	private final PagamentoSecundarioClient pagamentoSecundarioClient;
	
	public PagamentoService(PagamentoSecundarioClient pagamentoSecundarioClient) {
		this.pagamentoSecundarioClient = pagamentoSecundarioClient;
	}
	
    @CircuitBreaker(name = "pagamentoService", fallbackMethod = "fallbackProcessarPagamento")
    @Retry(name = "pagamentoService")
	public PagamentoResponse processarPagamento(PagamentoRequest pagamentoRequest) {
		logger.info("Processando pagamento: {}", pagamentoRequest);
		
		PagamentoResponse response = new PagamentoResponse();
		
		if(pagamentoRequest.getValor() <= 0) {
			throw new IllegalArgumentException("Valor invalido para pagamento.");
		}
		
		switch(pagamentoRequest.getTipoPagamento().toUpperCase()) {
		case "CREDITO":
			response.setStatus("APROVADO");
			response.setMensagem("Pagamento com cartão de crétido aprovado!");
			break;
		case "DEBITO":
			response.setStatus("APROVADO");
			response.setMensagem("Pagamento com cartão de débito aprovado!");
			break;
		case "PIX":
			response.setStatus("APROVADO");
			response.setMensagem("Pagamento por PIX aprovado!");
			break;
			default:
				throw new IllegalArgumentException("Tipo de pagamento invalido");
		}
		
		response.setId(UUID.randomUUID().toString());
		return response;
	}
	
	public PagamentoResponse fallbackProcessarPagamento(PagamentoRequest request, Throwable ex) {
		logger.warn("Erro ao processar pagamento princial. Tentando API secundária: {}", ex.getMessage());
		
		try {
			PagamentoResponse response = pagamentoSecundarioClient.processarPagamento(request);
			logger.info("Pagamento processado com sucesso pela API secundária: {}", response);
			return response;
		} catch (Exception e) {
			logger.error("Falha ao processar pagamenton na API secundária: {}", e.getMessage());
			
			PagamentoResponse response = new PagamentoResponse();
			response.setId(UUID.randomUUID().toString());
			response.setStatus("FALHA");
			response.setMensagem("O pagamento não pode ser processado pela API principal e nem pela secundária. Entre em contato com o suporte tecnico");
			return response;
		}
	}
}
