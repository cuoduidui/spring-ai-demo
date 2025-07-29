package com.cdd.demo.spring_ai_webFlux.application.web.client.chat;

import com.cdd.demo.spring_ai_webFlux.application.web.memory.ChatMemoryConfig;
import com.cdd.demo.spring_ai_webFlux.application.web.tools.server.GameOverToolService;
import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OllamaClientConfig {
    @Autowired
    private List<McpAsyncClient> mcpAsyncClients;
    @Autowired
    private GameOverToolService gameOverToolService;
    @Autowired
    private ChatMemory chatMemory;
    @Bean("ollamaClient")
    ChatClient ollamaClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem(
                        "你是游玩规划员。")
                .defaultAdvisors(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                //此时设置 工具方法 初始化时调用 不存在阻塞不阻塞
//                .defaultToolCallbacks(new AsyncMcpToolCallbackProvider(mcpAsyncClients))
//                .defaultTools(gameOverToolService)
                .build();
    }

    @Bean
     List<ToolCallback> toolCallbacks(List<McpAsyncClient> mcpAsyncClients){
        return Arrays.asList(new AsyncMcpToolCallbackProvider(mcpAsyncClients).getToolCallbacks());
    }
}
