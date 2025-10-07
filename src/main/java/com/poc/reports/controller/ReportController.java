package com.poc.reports.controller;

import com.poc.reports.dto.ReportDTO;
import com.poc.reports.dto.ReportFilterDto;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Report management APIs")
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
     * @return The created report with generated details (ReportId, Department, IssueDescription)
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<ReportEntity> createReport(@RequestBody ReportDTO report) {
        logger.debug("Start of REQUEST: POST /reports/create BODY: {}", report.toString() );
        ReportEntity response = reportService.createReport(report);
        logger.debug("END of /reports/create :: Response Code 200 :: Response: {}", response.toString());

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
    @PreAuthorize("hasAnyRole('USER','MANAGER')")
    public ResponseEntity<?> updateReport(@RequestBody ReportDTO report, @PathVariable String reportId) {
        logger.debug("Start of REQUEST: POST /reports/{}/update BODY: {}",reportId, report.toString() );
        ReportEntity response = reportService.updateReport(reportId, report);
        logger.debug("END of /reports/{}/update :: Response Code 200 :: Response: {}", reportId, response.toString());

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
        logger.debug("Start of REQUEST: POST /reports/export BODY: {}",deptNames.toString() );;
        byte[] excelFile = reportService.exportReportsToExcel(deptNames);
        logger.info("exportReportsToExcel():: Ends. Report exported successfully");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.xlsx");
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return ResponseEntity.ok()
                .header(String.valueOf(headers))
                .body(excelFile);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ReportEntity>> filterReports(@RequestBody ReportFilterDto filterDto){
        logger.debug("Start of REQUEST: GET /reports Parameters: {}, {}, {}", filterDto.getReportId(),
                filterDto.getCreatedFrom(), filterDto.getCreatedTo() );;
        List<ReportEntity> response = reportService.getReports(filterDto.getReportId(),filterDto.getCreatedFrom(),filterDto.getCreatedTo());
        logger.debug("END of /reports :: Response Code 200 :: Response: {}", response.toString());

        return ResponseEntity.ok(response);
    }

    /*@GetMapping
    public ResponseEntity<List<ReportEntity>> newFilter(@RequestBody ReportFilterDto rf){
        logger.debug("Start of REQUEST: GET /reports/filter Body: {}", rf.toString() );;

        List<ReportEntity> response =  reportService.newFilter(rf);
        logger.debug("END of /reports/filter :: Response Code 200 :: Response: {}", response.toString());

        return ResponseEntity.ok(response);

    }*/

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<List<ReportEntity>> getReportsBasedOnROle(@RequestParam String name) {
        logger.debug("Start of REQUEST: GET /reports/get Request Param: {}",name );;
        List<ReportEntity> response = reportService.getReportsBasedOnRole(name);
        logger.debug("END of /reports/get :: Response Code 200 :: Response: {}", response.toString());

        return ResponseEntity.ok(response);
    }

}
