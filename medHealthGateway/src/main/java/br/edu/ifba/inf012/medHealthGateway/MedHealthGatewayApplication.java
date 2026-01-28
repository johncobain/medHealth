package br.edu.ifba.inf012.medHealthGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MedHealthGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedHealthGatewayApplication.class, args);
	}

}
