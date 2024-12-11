package com.hexagonalexample.meiospagamento.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonalexample.meiospagamento.domain.model.PagamentoMetric;
import com.hexagonalexample.meiospagamento.domain.service.PagamentoService;
import com.hexagonalexample.meiospagamento.dto.PagamentoRequest;
import com.hexagonalexample.meiospagamento.dto.PagamentoResponse;
import com.hexagonalexample.meiospagamento.infrastructure.repository.PagamentoMetricRepository;

@Component
public class KafkaPagamentoListener {
	
    private static final Logger logger = LoggerFactory.getLogger(KafkaPagamentoListener.class);
    private final PagamentoService service;
    private final PagamentoMetricRepository metricRepository;
    private final ObjectMapper objectMapper;
    
    public KafkaPagamentoListener(PagamentoService service, PagamentoMetricRepository metricRepository, ObjectMapper objectMapper) {
    	this.service = service;
    	this.metricRepository = metricRepository;
    	this.objectMapper = objectMapper;
    }
    
    @KafkaListener(topics = "pagamentos", groupId = "pagamentos-group")
    public void processamentoPagamento(String menssagem) {
    	try {
			PagamentoRequest request = objectMapper.readValue(menssagem, PagamentoRequest.class);
			PagamentoMetric metric = new PagamentoMetric();
            PagamentoResponse response = service.processarPagamento(request);

			
			metric.setTipoPagamento(request.getTipoPagamento());
			metric.setValor(request.getValor());
			metric.setStatus(response.getStatus());
			metricRepository.save(metric);
			
			enviarNotificacao(response.getStatus(), response);
		} catch (Exception e) {
			logger.error("Erro ao processar o pagamento", e.getMessage());
		}
    }
    
    private void enviarNotificacao(String status, PagamentoResponse response) {
        logger.info("Notificação para status [{}]: {}", status, response);
    }
    

}
