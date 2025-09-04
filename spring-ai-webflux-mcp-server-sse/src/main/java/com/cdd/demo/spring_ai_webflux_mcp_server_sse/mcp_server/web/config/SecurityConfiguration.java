package com.cdd.demo.spring_ai_webflux_mcp_server_sse.mcp_server.web.config;

import com.cdd.demo.spring_ai_webflux_mcp_server_sse.mcp_server.web.util.RsaUtil;
import com.nimbusds.jose.crypto.impl.RSAKeyUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {
//    @Bean
//    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {
//        return http.authorizeExchange(exchanges -> exchanges
//                .pathMatchers("/**").authenticated()
//        )//要求所有请求必须经过认证，未认证请求将被拦截‌
////                .with(OAuth2AuthorizationServerConfigurer.authorizationServer(), Customizer.withDefaults())//启用默认授权服务器功能，包括令牌端点、授权端点等核心组件‌
//                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()))//配置资源服务器使用JWT令牌验证，默认采用NimbusJwtDecoder解析令牌‌
//                .csrf(csrf -> csrf
//                        .disable() // WebFlux中推荐全局禁用CSRF
//                )//禁用CSRF防护（适用于无状态API场景）‌
//                .cors(Customizer.withDefaults()).build();//启用默认CORS配置‌;
//    }
    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.csrf(csrf -> csrf.disable());
        http.authorizeExchange(ex -> ex
                .pathMatchers("/sse").permitAll()
                .pathMatchers("/mcp/*").permitAll()
                .anyExchange().authenticated()
        ).oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()));
        return http.build();
    }

    /**
     * 静态密钥场景
     * @return
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        RSAPublicKey publicKey = RsaUtil.getPublicKey("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKGvSlxFDyqMrjUbAqmYkg68wM5OOohjDpIZHdUOgBc2Mch8rgdbr2d3sm151ONLLycFLCFfi+OFm1jzQHRd+NUCAwEAAQ==");// 加载RSA公钥
        return NimbusReactiveJwtDecoder.withPublicKey(publicKey).build();
    }
//@Bean
//ReactiveJwtDecoder jwtDecoder() {
//    return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
//}
}