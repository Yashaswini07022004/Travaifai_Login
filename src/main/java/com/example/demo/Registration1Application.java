package com.example.demo;

import javax.crypto.SecretKey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Information"))
public class Registration1Application {
    public static void main(String[] args) {
        SpringApplication.run(Registration1Application.class, args);

    }
}