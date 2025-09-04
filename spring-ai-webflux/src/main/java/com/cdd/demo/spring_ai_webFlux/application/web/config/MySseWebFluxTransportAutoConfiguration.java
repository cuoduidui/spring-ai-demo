package com.cdd.demo.spring_ai_webFlux.application.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import org.springframework.ai.mcp.client.autoconfigure.NamedClientMcpTransport;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpClientCommonProperties;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpSseClientProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoConfiguration
@ConditionalOnClass(WebFluxSseClientTransport.class)
@EnableConfigurationProperties({ McpSseClientProperties.class, McpClientCommonProperties.class })
@ConditionalOnProperty(prefix = McpClientCommonProperties.CONFIG_PREFIX, name = "enabled", havingValue = "true",
        matchIfMissing = true)
public class MySseWebFluxTransportAutoConfiguration {

    /**
     * Creates a list of WebFlux-based SSE transports for MCP communication.
     *
     * <p>
     * Each transport is configured with:
     * <ul>
     * <li>A cloned WebClient.Builder with server-specific base URL
     * <li>ObjectMapper for JSON processing
     * <li>Server connection parameters from properties
     * </ul>
     * @param sseProperties the SSE client properties containing server configurations
     * @param webClientBuilderProvider the provider for WebClient.Builder
     * @param objectMapperProvider the provider for ObjectMapper or a new instance if not
     * available
     * @return list of named MCP transports
     */
    @Bean
    @Primary
    public List<NamedClientMcpTransport> webFluxClientTransports(McpSseClientProperties sseProperties,
                                                                 ObjectProvider<WebClient.Builder> webClientBuilderProvider,
                                                                 ObjectProvider<ObjectMapper> objectMapperProvider, ReactiveOAuth2AuthorizedClientManager clientManager) {

        List<NamedClientMcpTransport> sseTransports = new ArrayList<>();

        var webClientBuilderTemplate = webClientBuilderProvider.getIfAvailable(WebClient::builder);
        var objectMapper = objectMapperProvider.getIfAvailable(ObjectMapper::new);

        for (Map.Entry<String, McpSseClientProperties.SseParameters> serverParameters : sseProperties.getConnections().entrySet()) {
            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
                    new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientManager);
            oauth2Client.setDefaultOAuth2AuthorizedClient(true);
            var webClientBuilder = webClientBuilderTemplate.clone().baseUrl(serverParameters.getValue().url());
//            webClientBuilder.defaultHeader("Authorization", "Bearer " + clientManager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId(serverParameters.getKey()).principal("client").build()).getAccessToken().getTokenValue());
            webClientBuilder.filter(oauth2Client);
            String sseEndpoint = serverParameters.getValue().sseEndpoint() != null
                    ? serverParameters.getValue().sseEndpoint() : "/sse";
            var transport = WebFluxSseClientTransport.builder(webClientBuilder)
                    .sseEndpoint(sseEndpoint)
                    .objectMapper(objectMapper)
                    .build();
            sseTransports.add(new NamedClientMcpTransport(serverParameters.getKey(), transport));
        }

        return sseTransports;
    }
}
