package com.cdd.demo.spring_ai_webflux_mcp_server_stdio.mcp_server_stdio.web.tools.server;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class McpServerService {
    /**
     * 谷歌浏览器打开指定网址
     * @param path data
     * @return 无返回信息
     * @throws Exception if the request fails
     */
    @Tool(description = "谷歌浏览器打开指定网址")
    public void open(@ToolParam(description = "浏览器打开地址") String path) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("open", "-a", "Google Chrome", path);
        pb.redirectErrorStream(true); // 合并错误流
        Process process = pb.start();
        process.waitFor();
    }
    /**
     * 保存文件
     * @param content 内容
     * @return 地址
     * @throws Exception if the request fails
     */
    @Tool(description = "保存到本地并返回本地地址")
    public  String save(@ToolParam(description = "内容") String content) {
       File file = new File("/Users/yangfengshan2/IdeaProjects/demo-ai-webflux/demo-ai-webflux-mcp-server-stdio/src/main/resources/static/plan.html");
        try {
            // 创建父目录（如果不存在）
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // 使用FileWriter直接写入文件（会覆盖已存在的文件）
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(content);
                writer.flush();
            }

            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return "文件保存失败: " + e.getMessage();
        }
    }
}
