package com.cdd.demo.spring_ai_webFlux.application.web.config;

import org.springframework.ai.tool.execution.ToolExecutionExceptionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
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
    private ExchangeFilterFunction oauth2Credentials(OAuth2AuthorizedClientManager manager) {
        return (request, next) -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("mcp-server")
                    .principal("mcp-client")
                    .build();
            return Mono.fromSupplier(() -> manager.authorize(authorizeRequest))
                    .flatMap(client -> next.exchange(
                            ClientRequest.from(request)
                                    .headers(headers -> headers.setBearerAuth(client.getAccessToken().getTokenValue()))
                                    .build()
                    ));
        };
    }
}
