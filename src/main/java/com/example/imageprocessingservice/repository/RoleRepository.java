package com.example.imageprocessingservice.repository;

import com.example.imageprocessingservice.domain.ERole;
import com.example.imageprocessingservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(ERole name);
}
