package com.poc.reports.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReportFilterDto {
    String fieldName;

    String deptName;
    Date createdFrom;
    Date createdTo;

    String reportId;

    String issuePattern;

}
