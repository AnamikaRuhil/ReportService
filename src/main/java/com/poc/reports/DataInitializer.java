package com.poc.reports;

import com.poc.reports.controller.UserController;
import com.poc.reports.dto.DepartmentDTO;
import com.poc.reports.dto.UserDTO;
import com.poc.reports.models.DepartmentEntity;
import com.poc.reports.models.RoleEntity;
import com.poc.reports.service.DepartmentService;
import com.poc.reports.service.RoleService;
import com.poc.reports.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final DepartmentService departmentService;


    @Autowired
    public DataInitializer(UserService userService,
                           RoleService roleService,
                           DepartmentService departmentService) {
        this.userService = userService;
        this.roleService = roleService;
        this.departmentService = departmentService;
    }


    @Override
    public void run(String... args) throws Exception {
        RoleEntity userRole = roleService.createRole("user");
        RoleEntity adminRole = roleService.createRole("admin");


        DepartmentDTO departmentDTO = new DepartmentDTO("administration",null);
        DepartmentEntity adminDept = departmentService.createDepartment(departmentDTO);


        DepartmentDTO departmentDTO1 = new DepartmentDTO("HR",null);
        DepartmentEntity hr = departmentService.createDepartment(departmentDTO1);

        DepartmentDTO departmentDTO2 = new DepartmentDTO("sales",null);
        DepartmentEntity sales = departmentService.createDepartment(departmentDTO2);

        DepartmentDTO departmentDTO4 = new DepartmentDTO("finance",null);
        DepartmentEntity finance = departmentService.createDepartment(departmentDTO4);

        UserDTO userDTO = UserDTO.builder()
                .userName("admin")
                .email("admin@reportservice.com")
                .password("root")
                .roleId(adminRole.getId())
                .build();
        userService.createUser(userDTO);

    }
}
