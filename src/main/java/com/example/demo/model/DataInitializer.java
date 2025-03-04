package com.example.demo.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.repository.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!roleRepository.existsByName("ROLE_TRAVELLER")) {
            roleRepository.save(new Role("ROLE_TRAVELLER"));
        }

        if (!roleRepository.existsByName("ROLE_HOTELIER")) {
            roleRepository.save(new Role("ROLE_HOTELIER"));
        }

        if (!roleRepository.existsByName("ROLE_TRAVEL_AGENCY")) {
            roleRepository.save(new Role("ROLE_TRAVEL_AGENCY"));
        }

        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
    }
}