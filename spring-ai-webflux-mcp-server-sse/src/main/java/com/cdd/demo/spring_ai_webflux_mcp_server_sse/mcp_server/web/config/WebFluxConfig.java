package com.cdd.demo.spring_ai_webflux_mcp_server_sse.mcp_server.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class WebFluxConfig {
    @Bean
    public WebExceptionHandler streamErrorHandler() {
        return (exchange, ex) -> {
            if (ex instanceof IllegalStateException) {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                return exchange.getResponse().writeWith(
                        Flux.just(exchange.getResponse()
                                .bufferFactory().wrap("Stream processing failed".getBytes()))
                );
            }
            return Mono.error(ex);
        };
    }
}
