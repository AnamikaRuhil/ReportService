package com.poc.reports.controller;

import com.poc.reports.models.DepartmentEntity;
import com.poc.reports.service.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@Tag(name = "Department", description = " Department APIs")
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
        logger.debug("Start of REQUEST: POST /departments/create  for deptName: {}", name);
        DepartmentEntity response = departmentService.createDepartment(name);
        logger.debug("END of /departments/create:: StatusCode 200. Department created successfully {}", response.toString());
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @return list of  department
     */
    @GetMapping
    public ResponseEntity<List<DepartmentEntity>> getDepartments() {
        logger.debug("Start of REQUEST: GET /department");
        List<DepartmentEntity> response = departmentService.findAll();
        logger.debug("END of /department :: Response Code 200 :: Response: {}", response.toString());
        return ResponseEntity.ok(departmentService.findAll());
    }
}
