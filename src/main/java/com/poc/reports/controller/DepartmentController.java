package com.poc.reports.controller;

import com.poc.reports.dto.UserDTO;
import com.poc.reports.models.DepartmentEntity;
import com.poc.reports.models.UserEntity;
import com.poc.reports.service.DepartmentService;
import com.poc.reports.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Creates a new Department.
     *
     * @param name department name
     * @return created department details
     */
    @PostMapping("/create")
    public ResponseEntity<DepartmentEntity> createDepartment(@RequestBody String name) {
        logger.info("Starting createDepartment():: REQUEST: POST /departments/create  for deptName: {}", name);
        DepartmentEntity response = departmentService.createDepartment(name);
        logger.info("createDepartment():: Ends. Department created successfully");
        return ResponseEntity.ok(response);
    }
}
