package com.gbcreation.wall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class WallApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(WallApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(WallApplication.class, args);
		
		logger.info("Wall application has been initialized successfully.");
	}
}
