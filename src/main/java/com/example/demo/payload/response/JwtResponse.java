package com.example.demo.payload.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private List<String> roles;
    private String city; // New field for city
    private String dateOfBirth; // New field for date of birth

    public JwtResponse(String token) {
        this.token = token;
    }

    public JwtResponse(String token, String id, String username, String email, List<String> roles, String city, String dateOfBirth) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.city = city; // Set city
        this.dateOfBirth = dateOfBirth; // Set date of birth
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getCity() {
        return city; // Getter for city
    }

    public void setCity(String city) {
        this.city = city; // Setter for city
    }

    public String getDateOfBirth() {
        return dateOfBirth; // Getter for date of birth
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth; // Setter for date of birth
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", city='" + city + '\'' + // Include city in toString
                ", dateOfBirth='" + dateOfBirth + '\'' + // Include date of birth in toString
                '}';
    }
}