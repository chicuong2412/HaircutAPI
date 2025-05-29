package com.haircutAPI.HaircutAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HaircutApiApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(HaircutApiApplication.class, args);
	}

}