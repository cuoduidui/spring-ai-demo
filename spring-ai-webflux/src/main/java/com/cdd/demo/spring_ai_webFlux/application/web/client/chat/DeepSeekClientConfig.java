package com.cdd.demo.spring_ai_webFlux.application.web.client.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DeepSeekClientConfig {

    @Bean("deepSeekClient")
    ChatClient deepSeekClient(DeepSeekChatModel model) {
        return ChatClient.builder(model)
                .defaultSystem("你是游玩规划员。")
                .build();
    }
}
