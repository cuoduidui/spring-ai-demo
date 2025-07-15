package com.example.ai.demo_ai_webFlux.mcp_server.web.tools.server;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class McpServerService {


    /**
     * 通过时间获取一个行动代号
     * @param data data
     * @return 返回一个代号
     * @throws RestClientException if the request fails
     */
    @Tool(description = "通过时间获取一个行动代号")
    public String getActionMark(@ToolParam(description = "日期") String data) {
        return "代号：牛逼" + data;
    }
    /**
     * 通过时间获取一个行动代号
     * @param data data
     * @return 返回一个代号
     * @throws RestClientException if the request fails
     */
    @Tool(description = "通过时间获取一个行动代号",resultConverter = )
    public String getActionMark1(@ToolParam(description = "日期") String data) {
        return "代号：牛逼" + data;
    }
}
