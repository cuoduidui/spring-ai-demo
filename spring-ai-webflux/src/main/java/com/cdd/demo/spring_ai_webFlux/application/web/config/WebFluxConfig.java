package com.cdd.demo.spring_ai_webFlux.application.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
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
    @Bean
    ToolExecutionExceptionProcessor customExceptionProcessor() {
        return exception -> {
            // 返回友好的错误信息给模型
            return "操作失败：" + exception.getMessage();
        };
    }
}
