package com.t7solution.feedbacksystem.repository;

import com.t7solution.feedbacksystem.model.Role;
import com.t7solution.feedbacksystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByRole(Role role);
    User findByEmail(String email);
}