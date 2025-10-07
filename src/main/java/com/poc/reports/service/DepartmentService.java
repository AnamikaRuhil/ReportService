package com.poc.reports.service;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.logging.DataNotFoundException;
import com.poc.reports.models.DepartmentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository repository;
    @Autowired
    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    /**
     * Get department by id
     *
     * @param departmentId
     * @return  department
     */
    public  DepartmentEntity getDepartment(Long departmentId){
       Optional<DepartmentEntity> deptEntity =  repository.findById(departmentId);
       if(deptEntity.isPresent()){
           return deptEntity.get();
       }
       else{
           throw new DataNotFoundException("No Department found with department id ::" +departmentId);
       }
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

    /**
     * @return  list of departments
     */
    public List<DepartmentEntity> findAll() {
        return repository.findAll();
    }
}

