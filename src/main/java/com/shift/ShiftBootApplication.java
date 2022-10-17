package com.shift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author saito
 *
 */
@EnableScheduling
@SpringBootApplication
public class ShiftBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiftBootApplication.class, args);
	}
}
