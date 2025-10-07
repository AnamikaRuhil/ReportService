package com.poc.reports.controller;

import com.poc.reports.dto.UserDTO;
import com.poc.reports.dto.UserResponseDTO;
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
@Tag(name = "Users", description = "User Controller APIs")
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
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserDTO userDTO) {
        logger.debug("Start of REQUEST: POST /users/create Request Body: {}",userDTO.toString());;

        UserResponseDTO response = userService.createUser(userDTO);

        logger.info("END of /users/create ::  Response Code 200 :: Response: {}" , response);
        return ResponseEntity.ok(response);
    }
}

