package br.edu.ifba.inf012.medHealthNotifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableDiscoveryClient
public class MedHealthNotificationsApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.configure()
								.ignoreIfMissing()
								.load();
		
		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(MedHealthNotificationsApplication.class, args);
	}

}
