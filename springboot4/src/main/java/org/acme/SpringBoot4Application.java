package org.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
public class SpringBoot4Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot4Application.class, args);
	}

}
