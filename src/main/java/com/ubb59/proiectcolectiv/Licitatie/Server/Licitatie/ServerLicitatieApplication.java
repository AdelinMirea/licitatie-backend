package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ServerLicitatieApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerLicitatieApplication.class, args);
	}

}
