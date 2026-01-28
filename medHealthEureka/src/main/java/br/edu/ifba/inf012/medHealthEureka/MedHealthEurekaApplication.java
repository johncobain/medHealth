package br.edu.ifba.inf012.medHealthEureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MedHealthEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedHealthEurekaApplication.class, args);
	}

}
