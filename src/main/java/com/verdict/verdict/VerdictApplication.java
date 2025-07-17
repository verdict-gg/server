package com.verdict.verdict;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VerdictApplication {

	public static void main(String[] args) {
		SpringApplication.run(VerdictApplication.class, args);
	}
}