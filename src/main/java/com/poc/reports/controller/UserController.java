package com.poc.reports.controller;

import com.poc.reports.dao.UserRepository;
import com.poc.reports.dto.UserDTO;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.models.UserEntity;
import com.poc.reports.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.createUser(userDTO));
    }
}

