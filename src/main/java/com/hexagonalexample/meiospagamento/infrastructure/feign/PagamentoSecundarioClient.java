package com.hexagonalexample.meiospagamento.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.hexagonalexample.meiospagamento.dto.PagamentoRequest;
import com.hexagonalexample.meiospagamento.dto.PagamentoResponse;

@FeignClient(name = "pagamento-secundario", url = "http://api-secundaria:8081")
public interface PagamentoSecundarioClient {

	@PostMapping("/api/v1/processar-pagamento")
    PagamentoResponse processarPagamento(PagamentoRequest request);
}
