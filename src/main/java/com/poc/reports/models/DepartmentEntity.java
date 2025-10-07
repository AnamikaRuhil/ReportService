package com.poc.reports.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "department")
@NoArgsConstructor
public class DepartmentEntity extends BaseEntity{
    @Column(unique = true,nullable = false)
    private String name;

}
