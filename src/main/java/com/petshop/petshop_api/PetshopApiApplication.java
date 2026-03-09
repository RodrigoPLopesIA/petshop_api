package com.petshop.petshop_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class PetshopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetshopApiApplication.class, args);
	}
}
