package com.cdd.demo.spring_ai_webFlux.application.web.client.chat;

import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OllamaClientConfig {
    @Autowired
    private List<McpAsyncClient> mcpAsyncClients;
    @Bean("ollamaClient")
    ChatClient ollamaClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem(
                        "你是游玩规划员。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                //此时设置 工具方法 初始化时调用 不存在阻塞不阻塞
                .defaultToolCallbacks(new AsyncMcpToolCallbackProvider(mcpAsyncClients))
                .build();
    }
}
