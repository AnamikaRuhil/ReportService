package com.poc.reports.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

}
