package com.cdd.demo.spring_ai_webFlux.application.web.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class FilteredChatMemoryRepository implements ChatMemoryRepository {
    Map<String, List<Message>> chatMemoryStore = new ConcurrentHashMap();

    public List<String> findConversationIds() {
        return new ArrayList(this.chatMemoryStore.keySet());
    }

    public List<Message> findByConversationId(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        List<Message> messages = this.chatMemoryStore.get(conversationId);
        return (messages != null ? new ArrayList<Message>(messages) : List.of());
    }

    public void saveAll(String conversationId, List<Message> messages) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Assert.notNull(messages, "messages cannot be null");
        Assert.noNullElements(messages, "messages cannot contain null elements");
        List<Message> filtered = messages.stream()
                .filter(msg -> !msg.getText().contains("<think"))
                .collect(Collectors.toList());
        this.chatMemoryStore.put(conversationId, filtered);
    }

    public void deleteByConversationId(String conversationId) {
        log.info("清除对话记忆");
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        this.chatMemoryStore.remove(conversationId);
    }
}
