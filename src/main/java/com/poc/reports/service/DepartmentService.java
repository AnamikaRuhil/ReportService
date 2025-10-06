package com.poc.reports.service;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dto.DepartmentDTO;
import com.poc.reports.models.DepartmentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public  DepartmentEntity getDepartment(Long departmentId){
       return repository.findById(departmentId).get();
    }

    /**
     * Creates a new department.
     *
     * @param deptName  department object to be created
     * @return  created department
     */
    public DepartmentEntity createDepartment(String deptName) {
        DepartmentEntity dept = new DepartmentEntity();
        dept.setName( deptName);
        dept = repository.save(dept);
        logger.info("department created successfully!!!!");
        return dept;
    }
}

