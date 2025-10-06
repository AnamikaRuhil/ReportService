package com.poc.reports.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Document(collection = "reports")
@Getter
@Setter
@Table(name = "report")
public class ReportEntity {

    @Id
    private String id;
    @ManyToOne
    private String departmentName;
    private String issueDescription;
    private String updatedBy;
    private Date updatedAt;


}
