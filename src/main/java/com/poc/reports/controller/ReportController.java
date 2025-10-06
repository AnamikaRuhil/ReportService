package com.poc.reports.controller;

import com.poc.reports.dto.ReportDTO;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Reports management APIs")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Creates a new report.
     *
     * @param report The report object to be created
     * @return The created report with generated details (e.g., ReportId, Department, IssueDescription)
     */
    @PostMapping("/create")
    public ResponseEntity<ReportEntity> createReport(@RequestBody ReportDTO report) {
        logger.info("Starting createReport():: REQUEST: POST /reports/create  with body: {}", report);
        ReportEntity response = reportService.createReport(report);
        logger.info("createReport():: Ends. Report created successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Update existing report corresponding to report Id.
     *
     * @param report The report object with updated details
     * @param reportId to fetch existing report
     * @return Updated report
     */
    @PostMapping("/{reportId}/update")
    public ResponseEntity<?> updateReport(@RequestBody ReportDTO report, @PathVariable String reportId) {
        logger.info("Starting updateReport():: REQUEST: POST /reports/{reportId}/update for id {}", reportId);
        ReportEntity response = reportService.updateReport(reportId, report);
        logger.info("updateReport():: Ends. Report updated successfully");
        return ResponseEntity.ok(response);


    }

    /**
     * Export report to excels on the basis of departments.
     *
     * @param deptNames Report to be exported for these departments
     * @return Reports.xlsx
     */
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportReportsToExcel(@RequestBody List<String> deptNames) throws IOException {
        logger.info("Starting exportReportsToExcel():: REQUEST: POST /reports/export for department {}", deptNames);
        byte[] excelFile = reportService.exportReportsToExcel(deptNames);
        logger.info("exportReportsToExcel():: Ends. Report exported successfully");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.xlsx");
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return ResponseEntity.ok()
                .header(String.valueOf(headers))
                .body(excelFile);
    }

    @GetMapping
    public List<ReportEntity> findReports(@RequestParam (required =false) String id,
                                          @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
                                          @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate){
        return reportService.getReports(id,fromDate,toDate);
    }

}
