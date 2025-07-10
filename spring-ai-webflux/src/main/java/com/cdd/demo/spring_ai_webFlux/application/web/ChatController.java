package com.cdd.demo.spring_ai_webFlux.application.web;

import io.modelcontextprotocol.client.McpAsyncClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    @Resource(name = "deepSeekClient")
    private ChatClient deepSeekClient;
    @Resource(name = "ollamaClient")
    private ChatClient ollamaClient;
    @Resource(name = "responseFilter")
    private Function<ChatResponse, String> responseFilter;
    @Autowired
    private List<McpAsyncClient> mcpAsyncClients;
    /**
     * 多模型调用 流式输出
     * 可以推理模型帮你写图片描述，调图片生成模型生成图片
     *
     * @param message
     * @return
     */
    @RequestMapping("/chat")
    public Flux<String> chat1(String message) {
        Flux<String> firstFlux = ollamaClient
                .prompt()
                .toolCallbacks(new AsyncMcpToolCallbackProvider(mcpAsyncClients))
                .user(message).stream().content().cache();
        return firstFlux;
    }
}
