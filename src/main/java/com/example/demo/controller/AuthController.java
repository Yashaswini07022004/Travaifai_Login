package com.example.demo.controller;

import java.util.UUID;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Mail;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.services.MailService;
import com.example.demo.security.services.UserDetailsImpl;
import com.example.demo.security.services.UserDetailsServiceImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MailService emailService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Autowire the PasswordEncoder

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody LoginRequest loginRequest) {
        String identifier = loginRequest.getIdentifier();
        String password = loginRequest.getPassword();

        if (isValidEmail(identifier)) {
            Optional<User> userOptional = userRepository.findByEmail(identifier);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid email or password!"));
            }
        } else {
            Optional<User> userOptional = userRepository.findByUsername(identifier);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid username or password!"));
            }
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identifier, password)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid username or password!"));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "id", userDetails.getId(),
                "username", userDetails.getUsername(),
                "email", userDetails.getEmail(),
                "city", userDetails.getCity(),
                "dateOfBirth", userDetails.getDateOfBirth()
        ));
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser (@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        String verificationToken = UUID.randomUUID().toString();

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                hashedPassword,
                signUpRequest.getCity(),
                signUpRequest.getDateOfBirth(),
                false
        );

        user.setVerificationToken(verificationToken);

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName("ROLE_TRAVELLER");
            if (userRole == null) {
                throw new RuntimeException("Error: Role 'ROLE_TRAVELLER' is not found.");
            }
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                Role foundRole = roleRepository.findByName(role.toUpperCase());
                if (foundRole == null) {
                    throw new RuntimeException("Error: Role '" + role + "' is not found.");
                }
                roles.add(foundRole);
            });
        }
        user.setRoles(roles);
        userRepository.save(user);

        Mail mail = new Mail();
        mail.setMailFrom("verify9021@gmail.com");
        mail.setMailTo(user.getEmail());
        mail.setMailSubject("Email Verification");
        String verificationLink = "http://localhost:8086/api/auth/verify?token=" + verificationToken;
        mail.setMailContent("Please verify your email by clicking the following link: " + verificationLink);

        try {
            emailService.sendEmail(mail);
            return ResponseEntity.ok(new MessageResponse("User  registered successfully! Verification email sent."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("User  registered, but error sending verification email: " + e.getMessage()));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(true);
            user.setEmailVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("Email verified successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Invalid verification token!"));
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestPasswordReset(@Valid @RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is required!"));
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (!userOptional.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email not found!"));
        }

        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();
        user.setVerificationToken(resetToken);
        userRepository.save(user);

        Mail mail = new Mail();
        mail.setMailFrom("verify9021@gmail.com");
        mail.setMailTo(user.getEmail());
        mail.setMailSubject("Password Reset Request");
        String resetLink = "http://localhost:8086/api/auth/reset-password/" + resetToken;
        mail.setMailContent("To reset your password, click the following link: " + resetLink);

        try {
            emailService.sendEmail(mail);
            return ResponseEntity.ok(new MessageResponse("Password reset email sent successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Error sending password reset email: " + e.getMessage()));
        }
    }

    @PutMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @Valid @RequestBody Map<String, String> requestBody) {
        String newPassword = requestBody.get("newPassword");

        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: New password is required!"));
        }

        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid or expired token!"));
        }

        User user = userOptional.get();
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        user.setVerificationToken(null);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Password has been reset successfully!"));
    }
    @GetMapping("/hello")
    public String greet() {
        return "Welcome!!!!";
    }
}