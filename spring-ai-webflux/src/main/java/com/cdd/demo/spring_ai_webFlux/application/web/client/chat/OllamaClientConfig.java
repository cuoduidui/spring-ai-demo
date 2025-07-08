package com.cdd.demo.spring_ai_webFlux.application.web.client.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaClientConfig {
    @Bean("ollamaClient")
    ChatClient ollamaClient(OllamaChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem(
                        "你是游玩规划员。")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }
}
