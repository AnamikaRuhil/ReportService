package com.poc.reports.service;

import com.poc.reports.dao.RoleRepository;
import com.poc.reports.models.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

   @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleEntity createRole(String role){
       RoleEntity roleEntity = new RoleEntity();
       roleEntity.setRole(role);
       return roleRepository.save(roleEntity);
    }
}
