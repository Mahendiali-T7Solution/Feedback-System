package com.t7solution.feedbacksystem.service;

import com.t7solution.feedbacksystem.dto.RoleDto;
import com.t7solution.feedbacksystem.dto.UserDto;
import com.t7solution.feedbacksystem.model.Role;
import com.t7solution.feedbacksystem.model.User;
import com.t7solution.feedbacksystem.repository.RoleRepository;
import com.t7solution.feedbacksystem.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private ModelMapper modelMapper;


    public String authenticate(UserDto userdto) {
//        User user = modelMapper.map(userdto, User.class);

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(userdto.getEmail(), userdto.getPassword()));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(userdto.getEmail());
        }
        return "failure";
    }


}
