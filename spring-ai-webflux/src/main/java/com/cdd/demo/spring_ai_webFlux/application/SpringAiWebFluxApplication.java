package com.cdd.demo.spring_ai_webFlux.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication()
@EnableAsync
public class SpringAiWebFluxApplication {


	public static void main(String[] args) {
		SpringApplication.run(SpringAiWebFluxApplication.class, args);
	}
}
