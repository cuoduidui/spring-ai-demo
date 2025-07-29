package com.cdd.demo.spring_ai_webflux_mcp_server_sse.mcp_server.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())//要求所有请求必须经过认证，未认证请求将被拦截‌
                .with(OAuth2AuthorizationServerConfigurer.authorizationServer(), Customizer.withDefaults())//启用默认授权服务器功能，包括令牌端点、授权端点等核心组件‌
                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()))//配置资源服务器使用JWT令牌验证，默认采用NimbusJwtDecoder解析令牌‌
                .csrf(CsrfConfigurer::disable)//禁用CSRF防护（适用于无状态API场景）‌
                .cors(Customizer.withDefaults())//启用默认CORS配置‌
                .build();
    }
}