package com.poc.reports.service;

import com.poc.reports.dao.RoleRepository;
import com.poc.reports.models.RoleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

   @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    /**
     * Creates a new role.
     *
     * @param role  role to be created
     * @return  created role details
     */
    public RoleEntity createRole(String role){
        logger.debug("RoleService: createRole() starts for {}", role);
       RoleEntity roleEntity = new RoleEntity();
       roleEntity.setRole(role);
       roleEntity = roleRepository.save(roleEntity);
       logger.info("role created successfully!!!!");
        return roleEntity;
    }
}