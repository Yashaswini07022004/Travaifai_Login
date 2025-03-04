package com.example.demo.repository;


import javax.management.relation.RoleNotFoundException;


// Import Role model
import org.springframework.data.mongodb.repository.MongoRepository; // Import MongoRepository for MongoDB operations

import com.example.demo.model.EmployeeRole;
import com.example.demo.model.Role;


public interface RoleRepository extends MongoRepository<Role, String> 
{
	Role findByName(String name); 
    boolean existsByName(String name); 
}