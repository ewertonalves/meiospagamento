package com.hexagonalexample.meiospagamento.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hexagonalexample.meiospagamento.dto.PagamentoRequest;
import com.hexagonalexample.meiospagamento.dto.PagamentoResponse;

@RestController
@RequestMapping("/api/v1/pagamentos")
public class PagamentoController {
	
	 private static final Logger logger = LoggerFactory.getLogger(PagamentoController.class);
	    private final KafkaTemplate<String, PagamentoRequest> kafkaTemplate;

	    public PagamentoController(KafkaTemplate<String, PagamentoRequest> kafkaTemplate) {
	        this.kafkaTemplate = kafkaTemplate;
	    }

	    @PostMapping
	    public ResponseEntity<PagamentoResponse> processarPagamento(@RequestBody PagamentoRequest pagamentoRequest) {
	        logger.info("Recebendo solicitação de pagamento: {}", pagamentoRequest);

	        try {
	            kafkaTemplate.send("pagamentos", pagamentoRequest);
	            logger.info("Pagamento enviado para o Kafka com sucesso: {}", pagamentoRequest);

	            PagamentoResponse response = new PagamentoResponse();
	            response.setId(null);
	            response.setStatus("PENDENTE");
	            response.setMensagem("Pagamento recebido e será processado em breve.");
	            return ResponseEntity.ok(response);

	        } catch (Exception ex) {
	            logger.error("Erro ao enviar pagamento para o Kafka: {}", ex.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(new PagamentoResponse(null, "FALHA", "Erro ao processar a solicitação de pagamento."));
	        }
	    }

}
