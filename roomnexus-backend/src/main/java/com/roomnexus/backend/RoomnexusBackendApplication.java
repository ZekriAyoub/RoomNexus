package com.roomnexus.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RoomnexusBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomnexusBackendApplication.class, args);
	}

}
