package com.poc.reports;

import com.poc.reports.controller.UserController;
import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dao.RoleRepository;
import com.poc.reports.dto.DepartmentDTO;
import com.poc.reports.dto.UserDTO;
import com.poc.reports.models.DepartmentEntity;
import com.poc.reports.models.RoleEntity;
import com.poc.reports.service.DepartmentService;
import com.poc.reports.service.RoleService;
import com.poc.reports.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private final UserService userService;
    private final RoleService roleService;
    private final DepartmentService departmentService;

    private final DepartmentRepository departmentRepository;

    private final RoleRepository roleRepository;


    @Autowired
    public DataInitializer(UserService userService,
                           RoleService roleService,
                           DepartmentService departmentService, DepartmentRepository departmentRepository, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.departmentService = departmentService;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        logger.info("create default admin user :starts");

        String [] roles = {"ADMIN", "MANAGER","USER"};
        for(String role : roles){
            if (roleRepository.findByRole(role) == null) {
                roleService.createRole(role);
            }
        }

        String[] departments = {"IT", "HR", "Finance", "Operations", "Customer Support"};
        for (String deptName : departments) {
            if (departmentRepository.findByName(deptName) == null) {
                departmentService.createDepartment(deptName);
            }
        }

        UserDTO userDTO = UserDTO.builder()
                .userName("admin")
                .email("admin@reportservice.com")
                .password("root")
                .roleId(roleRepository.findByRole("ADMIN").getId())
                .build();
        userService.createUser(userDTO);
        logger.info("default admin user created successfully!!!!");

    }
}
