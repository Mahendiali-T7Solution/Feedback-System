package com.t7solution.feedbacksystem.repository;

import com.t7solution.feedbacksystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}