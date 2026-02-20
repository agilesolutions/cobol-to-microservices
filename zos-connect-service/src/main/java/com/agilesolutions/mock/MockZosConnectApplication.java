package com.agilesolutions.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class MockZosConnectApplication {

	public static void main(String[] args) {

		SpringApplication.run(MockZosConnectApplication.class, args);
	}
}
