package com.manager.coopafi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoopafiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoopafiApplication.class, args);
	}

}
