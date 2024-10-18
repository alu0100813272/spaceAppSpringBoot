package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaching // Activa cache.
public class SpaceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpaceAppApplication.class, args);
	}

}
