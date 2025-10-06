package com.poc.reports.dao;

import com.poc.reports.models.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
    Collection<Object> findByNameIn(List<String> departmentNames);

    DepartmentEntity findByName(String name);
}
