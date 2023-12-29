package com.service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Java code for a Service Registry Application.
 * This code represents a Spring Boot application that serves as a Eureka Server.
 * The @SpringBootApplication annotation indicates that this class is the main entry point of the application.
 * The @EnableEurekaServer annotation enables the Eureka Server functionality.
 */

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryApplication.class, args);
	}

}
