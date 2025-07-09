package com.cdd.demo.spring_ai_webFlux.application.web.config;


import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.mcp.client.autoconfigure.McpClientAutoConfiguration;
import org.springframework.ai.mcp.client.autoconfigure.NamedClientMcpTransport;
import org.springframework.ai.mcp.client.autoconfigure.StdioTransportAutoConfiguration;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpClientCommonProperties;
import org.springframework.ai.mcp.client.autoconfigure.properties.McpStdioClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AutoConfiguration(before = {McpClientAutoConfiguration.class})
@ConditionalOnClass({McpSchema.class})
@EnableConfigurationProperties({McpStdioClientProperties.class, McpClientCommonProperties.class})
@ConditionalOnProperty(
        prefix = "spring.ai.mcp.client",
        name = {"enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class MyStdioTransportAutoConfiguration extends StdioTransportAutoConfiguration {
    @Bean
    @Primary
    public List<NamedClientMcpTransport> stdioTransports(McpStdioClientProperties stdioProperties) {
        List<NamedClientMcpTransport> stdioTransports = new ArrayList();

        for(Map.Entry<String, ServerParameters> serverParameters : stdioProperties.toServerParameters().entrySet()) {
            MyStdioClientTransport transport = new MyStdioClientTransport((ServerParameters)serverParameters.getValue());
            stdioTransports.add(new NamedClientMcpTransport((String)serverParameters.getKey(), transport));
        }

        return stdioTransports;
    }
}