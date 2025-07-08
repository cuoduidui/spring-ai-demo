package com.cdd.demo.spring_ai_webFlux.application.web.client.filter;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;
@Configuration
public class ResponseFilterConfig {
    @Bean("responseFilter")
    public Function<ChatResponse, String> responseFilter() {
        return response -> {
            String rawContent=response.getResult().getOutput().getText(); // 仅提取最终答案
            // 2. 移除思考内容（根据实际返回结构调整）
            String msg= null;
            if (rawContent != null) {
                msg = rawContent.replaceAll("思考过程:.*?结论:", "")
                        .replaceAll("<think>[\\s\\S]*?</think>", "") // 正则移除中间内容
                        .replace("内部推理:", "");
            }
            System.out.println(msg);
            return msg;
        };
    }
}
