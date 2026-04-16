package com.optima.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class OptimaAdapter {

	public static void main(String[] args) {
		SpringApplication.run(OptimaAdapter.class, args);
		log.info("Optima CMS adapter application has started...");
	}
}
