package com.poc.reports.controller;

import com.poc.reports.dao.UserRepository;
import com.poc.reports.dto.UserDTO;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.models.UserEntity;
import com.poc.reports.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User Controller API")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Creates a new user.
     *
     * @param userDTO user object to be created
     * @return created user details
     */
    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDTO userDTO) {
        logger.info("Starting createUser():: REQUEST: POST /users/create  with body: {}", userDTO);
        UserEntity response = userService.createUser(userDTO);
        logger.info("createUser():: Ends. User created successfully");
        return ResponseEntity.ok(response);
    }
}

