package com.starking.jobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class JobsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(JobsApplication.class, args);
		run.close();
	}
}
