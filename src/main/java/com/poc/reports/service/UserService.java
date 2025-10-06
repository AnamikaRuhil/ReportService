package com.poc.reports.service;

import com.poc.reports.dao.RoleRepository;
import com.poc.reports.dao.UserRepository;
import com.poc.reports.dto.UserDTO;
import com.poc.reports.models.RoleEntity;
import com.poc.reports.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository repo, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = repo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserEntity findByUsername(String userName) {
        return userRepository.findByUsername(userName).orElse(null);
    }

    public UserEntity createUser(UserDTO userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUserName());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        //userEntity.setPassword(userDto.getPassword());
        userEntity.setEmail(userDto.getEmail());
        Optional<RoleEntity> role = roleRepository.findById(userDto.getRoleId());
        if (role.isPresent()) {
            RoleEntity roleEntity = role.get();

            userEntity.setRoleEntity(roleEntity);
        } else {
            throw new RuntimeException("Role not found");
        }


        return userRepository.save(userEntity);
    }
}
