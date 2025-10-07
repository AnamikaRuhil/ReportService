package com.poc.reports.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "users")

public class UserEntity extends BaseEntity {
    @Column(unique = true,nullable = false)
    private String username;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleEntity;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;
}
