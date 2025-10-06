package com.poc.reports.models;

import com.poc.reports.service.DepartmentService;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Table(name = "department")
@NoArgsConstructor
public class DepartmentEntity extends BaseEntity{
    @Column(unique = true,nullable = false)
    private String name;

}
