package com.example.demo.Oauthhandler;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Spring OAuth2 Demo API")
                        .version("1.0")
                        .description("API documentation for Spring OAuth2 Demo"))
                .addSecurityItem(new SecurityRequirement().addList("oauth2"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("oauth2",
                                new SecurityScheme()
                                        .type(Type.OAUTH2)
                                        .in(In.HEADER)
                                        .name("Authorization")
                                        .description("OAuth2 Authorization header")
                                        .flows(new io.swagger.v3.oas.models.security.OAuthFlows()
                                                .authorizationCode(new io.swagger.v3.oas.models.security.OAuthFlow()
                                                        .authorizationUrl("http://localhost:8086/oauth/authorize")
                                                        .tokenUrl("http://localhost:8086/oauth/token") 
                                                        .scopes(new io.swagger.v3.oas.models.security.Scopes()
                                                                .addString("read", "Read access")
                                                                .addString("write", "Write access"))))));
    }
}