package com.t7solution.feedbacksystem.service;

import com.t7solution.feedbacksystem.dto.RoleDto;
import com.t7solution.feedbacksystem.dto.UserDto;
import com.t7solution.feedbacksystem.model.Role;
import com.t7solution.feedbacksystem.model.User;
import com.t7solution.feedbacksystem.repository.RoleRepository;
import com.t7solution.feedbacksystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void saveUser(UserDto userDto, RoleDto roleDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = modelMapper.map(roleDto, Role.class);
        user.setRole(role);

        // Set manager if managerId is provided
        if (userDto.getManagerId() != null) {
            Optional<User> managerOpt = userRepository.findById(userDto.getManagerId());
            if (managerOpt.isPresent()) {
                user.setManager(managerOpt.get());
            } else {
                throw new RuntimeException("Manager not found with ID: " + userDto.getManagerId());
            }
        }else {
            Role adminRole = roleRepository.findByName("Admin");

            // Find a user with the Admin role
            User adminUser = userRepository.findFirstByRole(adminRole);

            user.setManager(adminUser);
        }
        System.out.println("user information: " + user);

        userRepository.save(user);

    }
}
