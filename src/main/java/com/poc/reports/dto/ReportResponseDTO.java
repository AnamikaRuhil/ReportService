package com.poc.reports.dto;

import com.poc.reports.models.ReportEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ReportResponseDTO {

    public final int statusCode;

    public final String message;

    public final ReportEntity entity;


}
