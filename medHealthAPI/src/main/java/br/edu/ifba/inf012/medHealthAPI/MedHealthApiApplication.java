package br.edu.ifba.inf012.medHealthAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MedHealthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedHealthApiApplication.class, args);
	}

}
