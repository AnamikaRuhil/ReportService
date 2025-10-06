package com.poc.reports.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDTO {

    private long departmentId;
    private String issueDescription;
    private String departmentName;

}
