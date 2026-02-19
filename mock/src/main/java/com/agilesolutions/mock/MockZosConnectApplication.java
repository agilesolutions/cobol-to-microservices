package com.agilesolutions.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class MockZosConnectApplication {

	public static void main(String[] args) {

		SpringApplication.run(MockZosConnectApplication.class, args);
	}
}
