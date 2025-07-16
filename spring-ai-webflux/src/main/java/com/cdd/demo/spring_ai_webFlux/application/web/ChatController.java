package com.cdd.demo.spring_ai_webFlux.application.web;

import com.cdd.demo.spring_ai_webFlux.application.web.tools.server.GameOverToolService;
import io.modelcontextprotocol.client.McpAsyncClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
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
//    @Autowired
//    private List<ToolCallback> toolCallbacks;
    @Autowired
    private GameOverToolService gameOverToolService;
    /**
     * 多模型调用 流式输出
     * 可以推理模型帮你写图片描述，调图片生成模型生成图片
     *
     * @param message
     * @return
     */
    @RequestMapping("/chat")
    public Flux<String> chat1(String message) {
//        ToolCallback[] dateTimeTools = ToolCallbacks.from(new GameOverToolService());
//        ChatOptions chatOptions = ToolCallingChatOptions.builder()
//                .toolCallbacks(dateTimeTools)
//                .build();
        Flux<String> firstFlux = ollamaClient
                .prompt("name is 小智")
//                .toolCallbacks(toolCallbacks)
                .tools(gameOverToolService)
                .toolContext(Map.of("name", "小智"))
                .user(message).stream().content().cache();
        return firstFlux;
    }
}
