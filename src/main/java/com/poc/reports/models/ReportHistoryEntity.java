package com.poc.reports.models;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@Document(collection = "reporthistory")
@Getter
@Setter
public class ReportHistoryEntity {
    @Id
    public String id;
    public String reportId;
    public String actionType;
    public String updatedBy;
    public Date updatedAt;
    public HashMap<String, String> oldReportData;
    public HashMap<String, String> newReportData;
}
