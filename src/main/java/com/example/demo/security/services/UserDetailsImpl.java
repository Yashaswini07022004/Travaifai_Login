package com.example.demo.security.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String email;

    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean emailVerified; // Field for email verification
    private String city; // New field for city
    private Date dateOfBirth; // New field for date of birth

    public UserDetailsImpl(String id, String username, String email, String password,
                            boolean emailVerified,
                           String city, Date date) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.emailVerified = emailVerified; // Initialize email verification status
        this.city = city; // Initialize city
        this.dateOfBirth = date; // Initialize date of birth
    }

    public static UserDetailsImpl build(com.example.demo.model.User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.isEmailVerified(), // Pass email verification status
                user.getCity(), // Pass city
                user.getDateOfBirth() // Pass date of birth
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Return authorities
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return emailVerified; // Check if the email is verified
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true; // Check if the same reference
        }
        if (o == null || getClass() != o.getClass()) {
            return false; // Check for null and class type
        }
        // Use instanceof to safely cast
        if (!(o instanceof UserDetailsImpl)) {
            return false; // Ensure o is an instance of UserDetailsImpl
        }
        UserDetailsImpl user = (UserDetailsImpl) o; // Safe cast
        return Objects.equals(id, user.id); // Compare IDs
    }

    public boolean isEmailVerified() {
        return emailVerified; // Return the email verification status
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified; // Setter to update the email verification status
    }

    public String getCity() {
        return city; // Getter for city
    }

    public void setCity(String city) {
        this.city = city; // Setter for city
    }

    public Date getDateOfBirth() {
        return dateOfBirth; // Getter for date of birth
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth; // Setter for date of birth
    }

    @Override
    public String toString() {
        return "User DetailsImpl{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", city='" + city + '\'' + // Include city in toString
                ", dateOfBirth='" + dateOfBirth + '\'' + // Include date of birth in toString
                '}';
    }
}