package com.poc.reports.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
@RequiredArgsConstructor
public class ReportHistoryDTO {
    public String reportId;
    public String actionType;
    public String updatedBy;
    public Date updatedAt;
    public HashMap<String, String> oldReportData;
    public HashMap<String, String> newReportData;
}
