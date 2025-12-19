package com.jovij.OpenClinic;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "OpenClinic API", version = "1.0", description = "API for OpenClinic"))
public class OpenClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenClinicApplication.class, args);
	}

}
