package com.poc.reports.service;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dao.ReportHistoryRepository;
import com.poc.reports.dao.ReportRepository;
import com.poc.reports.dto.ReportDTO;
import com.poc.reports.logging.DataNotFoundException;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.models.ReportHistoryEntity;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@NoArgsConstructor(force = true)
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository repository;

    private final DepartmentService departmentService;

    public final ReportHistoryRepository reportHistoryRepository;

    public final DepartmentRepository deptRepository;

    @Autowired
    public ReportService(DepartmentService departmentService, ReportRepository repository, ReportHistoryRepository reportHistoryRepository, DepartmentRepository deptRepository) {
        this.departmentService = departmentService;
        this.repository = repository;
        this.reportHistoryRepository = reportHistoryRepository;
        this.deptRepository = deptRepository;
    }

    /**
     * Creates a new report and saves it to the database.
     *
     * @param reportDto Report object to be created
     * @return The created report
     */
    public ReportEntity createReport(ReportDTO reportDto) {
        logger.debug("ReportService: createReport() starts for {}", reportDto);
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setDepartmentName(departmentService.getDepartment(reportDto.getDepartmentId()).getName());
        reportEntity.setIssueDescription(reportDto.getIssueDescription());
        reportEntity.setCreatedAt(new Date());
        reportEntity.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        repository.save(reportEntity);
        logger.debug("ReportService: createReport() ends. Report saved to database.");

        logger.debug("ReportService: createReport() starts for reportHistory ");

        ReportHistoryEntity history = new ReportHistoryEntity();

        history.setReportId(reportEntity.getId());
        history.setUpdatedAt(new Date());
        history.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        history.setActionType("CREATE");
        history.setNewReportData(new HashMap<>());
        history.getNewReportData().put("department", reportEntity.getDepartmentName());
        history.getNewReportData().put("issueDescription", reportDto.getIssueDescription());

        reportHistoryRepository.save(history);
        logger.debug("ReportService: createReport() ends. ReportHistory saved to database.");
        return reportEntity;
    }

    /**
     * Updates existing report and creates report history for updated report details.
     *
     * @param reportDTO Report object to be updated
     * @param reportId Id of report
     * @return updated report
     */
    public ReportEntity updateReport(String reportId, ReportDTO reportDTO) {
        ReportEntity reportEntity = null;
        logger.debug("ReportService: updateReport() starts::");
        if (repository != null) {
            Optional<ReportEntity> optionalReportEntity = repository.findById(reportId);

            if (optionalReportEntity.isPresent()) {
                reportEntity = optionalReportEntity.get();
            } else {
                throw new DataNotFoundException("Report Not Found for id :" + reportId);
            }
        }

        ReportHistoryEntity history = new ReportHistoryEntity();

        history.setReportId(reportId);
        history.setUpdatedAt(new Date());
        history.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        history.setActionType("UPDATE");

        history.setOldReportData(new HashMap<>());
        history.setNewReportData(new HashMap<>());

        String newDepartmentName = departmentService.getDepartment(reportDTO.getDepartmentId()).getName();
        if (!reportEntity.getDepartmentName().equals(newDepartmentName)) {
            history.getOldReportData().put("department", reportEntity.getDepartmentName());
            history.getNewReportData().put("department", newDepartmentName);

        }
        if (!reportEntity.getIssueDescription().equals(reportDTO.getIssueDescription())) {
            history.getOldReportData().put("issueDescription", reportEntity.getIssueDescription());
            history.getNewReportData().put("issueDescription", reportDTO.getIssueDescription());
        }

        reportEntity.setIssueDescription(reportDTO.getIssueDescription());
        reportEntity.setDepartmentName(newDepartmentName);
        reportEntity.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        repository.save(reportEntity);
        reportHistoryRepository.save(history);

        logger.debug("ReportService: updateReport() ends. Report and ReportHistory updated successfully ");
        return reportEntity;
    }

    /**
     * Exports reports for the given list of department names to an Excel file.
     * </p>Method fetches all reports corresponding to the provided department names,
     *  and  generates an Excel workbook containing the report data, and returns it as a byte array.</p>
     *
     * @param deptNames List of department names for which reports need to be exported
     * @return byte array representing the generated Excel file (.xlsx)
     * @throws IOException If an error occurs while creating or writing the Excel file
     */
    public byte[] exportReportsToExcel(List<String> deptNames) throws IOException {
        List<ReportEntity> reports = repository.findByDepartmentNameIgnoreCaseIn(deptNames);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reports");

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);

            // Header Row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Department Name", "Issue Description"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowIdx = 1;
            for (ReportEntity report : reports) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(report.getId());
                row.createCell(1).setCellValue(report.getDepartmentName());
                row.createCell(2).setCellValue(report.getIssueDescription());
            }

            for(int i = 0; i < headers.length; i++){
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream("Reports.xlsx")) {
                workbook.write(fileOut);
            } catch (IOException e)
            { e.printStackTrace();
            }
            //workbook.write(out);
            workbook.close();
            return out.toByteArray();
        }
    }

    /**
     * Method returns list of reports based on reportId and DateOfCreation of Report
     *
     * @param id reportId for filtering
     * @param fromDate start date for filtering
     * @param toDate   end date for filtering
     * @return list of reports filtered on id and date
     */
    public List<ReportEntity> getReports(String id, Date fromDate, Date toDate) {
        if (id != null) {
            return repository.findById(id)
                    .map(List::of)
                    .orElseThrow(() -> new DataNotFoundException(id));
        } else if (fromDate != null && toDate != null) {
            List<ReportEntity> reports = repository.findByCreatedAtBetween(fromDate, toDate);
            if (reports.isEmpty()) {
                throw new DataNotFoundException("No reports found in date range: " + fromDate + " to " + toDate);
            }
            return reports;
        }

        return repository.findAll();

    }

}
