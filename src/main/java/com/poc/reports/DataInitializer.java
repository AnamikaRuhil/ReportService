package com.poc.reports;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dao.RoleRepository;
import com.poc.reports.dto.UserDTO;
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
    public void run(String... args) {
        initializeRoles();
        initializeDepartments();
        initializeUsers();
    }

    private void initializeUsers() {
        UserDTO adminUserDTO = UserDTO.builder()
                .userName("testadmin")
                .email("admin@reportservice.com")
                .password("root")
                .roleId(roleRepository.findByRole("ADMIN").getId())
                .departmentId(departmentRepository.findByNameIgnoreCase("HR").getId())
                .build();
        userService.createUser(adminUserDTO);

        UserDTO userUserDTO = UserDTO.builder()
                .userName("testuser")
                .email("user@reportservice.com")
                .password("root")
                .roleId(roleRepository.findByRole("USER").getId())
                .departmentId(departmentRepository.findByNameIgnoreCase("IT").getId())
                .build();
        userService.createUser(userUserDTO);

        UserDTO managerUserDTO = UserDTO.builder()
                .userName("testmanager")
                .email("manager@reportservice.com")
                .password("root")
                .roleId(roleRepository.findByRole("MANAGER").getId())
                .departmentId(departmentRepository.findByNameIgnoreCase("Finance").getId())
                .build();
        userService.createUser(managerUserDTO);
        logger.info("default users created successfully!!!!");

    }

    private void initializeDepartments() {
        String[] departments = {"IT", "HR", "Finance", "Operations", "Customer Support"};
        for (String deptName : departments) {
            if (departmentRepository.findByNameIgnoreCase(deptName) == null) {
                departmentService.createDepartment(deptName);
            }
        }
    }

    private void initializeRoles() {
        String [] roles = {"ADMIN", "MANAGER","USER"};
        for(String role : roles){
            if (roleRepository.findByRole(role) == null) {
                roleService.createRole(role);
            }
        }
    }
}
