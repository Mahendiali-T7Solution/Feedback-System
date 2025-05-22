package com.t7solution.feedbacksystem.service;

import com.t7solution.feedbacksystem.dto.RoleDto;
import com.t7solution.feedbacksystem.model.Role;
import com.t7solution.feedbacksystem.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;
    public RoleDto findRoleById(Long roleId){
        Role role = roleRepository.findById(roleId).orElse(null);
        return modelMapper.map(role, RoleDto.class);
    }

}
