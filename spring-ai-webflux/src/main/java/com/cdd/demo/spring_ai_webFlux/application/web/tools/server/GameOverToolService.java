package com.cdd.demo.spring_ai_webFlux.application.web.tools.server;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameOverToolService {

    @Autowired
    private ChatMemory chatMemory;
//     @Tool(description = "结束",resultConverter = CustomToolCallResultConverter.class) CustomToolCallResultConverter 类型转换器 可实现 ToolCallResultConverter
//    当使用声明式方法从方法构建工具时，您可以通过将注释returnDirect的属性设置@Tool为来标记工具将结果直接返回给调用者true。
    @Tool(description = "结束回话", returnDirect = true)
    public String gameOver(ToolContext toolContext) {
        chatMemory.clear("chat_memory_conversation_id1");
        return toolContext.getContext().get("name")+"推荐结束";
    }
}
