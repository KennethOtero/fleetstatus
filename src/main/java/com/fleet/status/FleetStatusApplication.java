package com.fleet.status;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FleetStatusApplication {

	public static void main(String[] args) {
		SpringApplication.run(FleetStatusApplication.class, args);
	}

}
