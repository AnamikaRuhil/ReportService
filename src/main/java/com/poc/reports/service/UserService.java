package com.poc.reports.service;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dao.RoleRepository;
import com.poc.reports.dao.UserRepository;
import com.poc.reports.dto.UserDTO;
import com.poc.reports.dto.UserResponseDTO;
import com.poc.reports.exception.DataNotFoundException;
import com.poc.reports.models.DepartmentEntity;
import com.poc.reports.models.RoleEntity;
import com.poc.reports.models.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository repo, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        this.userRepository = repo;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Creates a new user.
     *
     * @param userDto  user object to be created
     * @return  created user
     */
    public UserResponseDTO createUser(UserDTO userDto) {
        logger.debug("UserService: createUser() starts for {}", userDto);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUserName());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEmail(userDto.getEmail());
        Optional<RoleEntity> role = roleRepository.findById(userDto.getRoleId());
        if (role.isPresent()) {
            RoleEntity roleEntity = role.get();

            userEntity.setRoleEntity(roleEntity);
        } else {
            logger.warn("No Role found during user creation with role id::" + userDto.getRoleId());
            throw new DataNotFoundException("No Role found with role id ::" + userDto.getRoleId());
        }

        Optional<DepartmentEntity> dept = departmentRepository.findById(userDto.getDepartmentId());
        if (dept.isPresent()) {
            DepartmentEntity departmentEntity = dept.get();

            userEntity.setDepartment(departmentEntity);
        } else {
            logger.warn("No Department found  during user creation with department id::" + userDto.getDepartmentId());
            throw new DataNotFoundException("No Department found with department id ::" + userDto.getDepartmentId());
        }

        userEntity = userRepository.save(userEntity);
        UserResponseDTO responseDTO = UserResponseDTO.builder()
                .id(userEntity.getId())
                .userName(userEntity.getUsername())
                .email(userEntity.getEmail())
                .departmentName(userEntity.getDepartment().getName())
                .role(userEntity.getRoleEntity().getRole())
                .build();
        logger.debug("user created successfully!!!!");
        return responseDTO;
    }
}
