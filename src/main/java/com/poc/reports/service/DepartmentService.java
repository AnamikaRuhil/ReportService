package com.poc.reports.service;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dto.DepartmentDTO;
import com.poc.reports.models.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public  DepartmentEntity getDepartment(Long departmentId){
       return repository.findById(departmentId).get();
    }
    public DepartmentEntity createDepartment(DepartmentDTO department) {
        DepartmentEntity dept = new DepartmentEntity();
        dept.setName(department.getName());
        dept.setDepartmentHead(department.getDepartmentHead());
        return repository.save(dept);
    }
}
