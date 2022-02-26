package edu.xidian.pnaWeb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PnaWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(PnaWebApplication.class, args);
	}
}
