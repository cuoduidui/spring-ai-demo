package com.cdd.demo.spring_ai_webflux_mcp_server_stdio.mcp_server_stdio;

import com.cdd.demo.spring_ai_webflux_mcp_server_stdio.mcp_server_stdio.web.tools.server.McpServerService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoAiWebFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoAiWebFluxApplication.class, args);
	}
	@Bean
	public ToolCallbackProvider weatherTools(McpServerService mcpServerService) {
		return MethodToolCallbackProvider.builder().toolObjects(mcpServerService).build();
	}
}
