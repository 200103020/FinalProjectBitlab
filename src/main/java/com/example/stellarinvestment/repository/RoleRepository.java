package com.example.stellarinvestment.repository;

import com.example.stellarinvestment.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String roleName);
}
