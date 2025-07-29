package com.cdd.demo.spring_ai_webflux_mcp_server_sse.mcp_server;

import com.cdd.demo.spring_ai_webflux_mcp_server_sse.mcp_server.web.tools.server.McpServerService;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
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

	public record TextInput(String input) {
	}

	@Bean
	public ToolCallback toUpperCase() {
		return FunctionToolCallback.builder("toUpperCase", (TextInput input) -> input.input().toUpperCase())
				.inputType(TextInput.class)
				.description("Put the text to upper case")
				.build();
	}
}
