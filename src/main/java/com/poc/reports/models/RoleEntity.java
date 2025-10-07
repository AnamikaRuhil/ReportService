package com.poc.reports.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "role")
public class RoleEntity extends BaseEntity {
    @Column(unique = true,nullable = false)
    private String role;
}
