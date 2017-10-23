package com.ken.gae.quiz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.ken.gae.quiz.config.EnableSSLConfiguration;

@SpringBootApplication
@Import({ EnableSSLConfiguration.class })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
