package com.poc.reports.models;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
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
    private Date createdAt;
    private String createdBy;


}
