package com.poc.reports.controller;

import com.poc.reports.dao.ReportRepository;
import com.poc.reports.dto.ReportDTO;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Reports management APIs")
public class ReportController {
    private final ReportService reportService;


    private final ReportRepository repository;

    public ReportController(ReportService reportService,ReportRepository repository){
        this.reportService = reportService;
        this.repository = repository;
    }

    @PostMapping("/create")
    public ResponseEntity<ReportEntity> createReport(@RequestBody ReportDTO report) {
        return ResponseEntity.ok(reportService.createReport(report));
    }


    @PostMapping("/{reportId}/update")
    public ResponseEntity<?> updateReport(@RequestBody ReportDTO report, @PathVariable String reportId) {
        try {
            return ResponseEntity.ok(reportService.updateReport(reportId, report));
        }
        catch(RuntimeException e)
        {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

   // @GetMapping("/export")
   @PostMapping("/export")
    public ResponseEntity<byte[]> exportReportsToExcel(@RequestBody List<String> deptNames) throws IOException {
        byte[] excelFile = reportService.exportReportsToExcel(deptNames);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
    }



}
