package com.example.demo.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.Oauthhandler.CustomOAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] WHITE_LIST_URL = { 
        "/api/auth/**", 
        "/api/v1/auth/**", 
        "/v2/api-docs", 
        "/v3/api-docs", 
        "/v3/api-docs/**", 
        "/swagger-resources", 
        "/swagger-resources/**", 
        "/configuration/ui", 
        "/configuration/security", 
        "/swagger-ui/**", 
        "/webjars/**", 
        "/swagger-ui.html", 
        "/api/test/**", 
        "/authenticate",
        "/public/**",
        "/error",
        "/login",
        "/actuator/**",
       "/api/auth/reset-password/**"
    };

    @Autowired
    private CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // Change to IF_REQUIRED
            .authorizeRequests(auth -> auth
                .requestMatchers(WHITE_LIST_URL).permitAll() 
                .anyRequest().authenticated()) 
            .oauth2Login()
                .loginPage("/login")  
                .successHandler(customOAuth2LoginSuccessHandler)
                .permitAll() 
            .and()
            .formLogin()
                .disable() 
            .logout()
                .permitAll();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8086"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}