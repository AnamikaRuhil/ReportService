package com.poc.reports.service;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dao.ReportHistoryRepository;
import com.poc.reports.dao.ReportRepository;
import com.poc.reports.dto.ReportDTO;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.models.ReportHistoryEntity;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

    public ReportEntity createReport(ReportDTO reportDto) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setDepartmentName(departmentService.getDepartment(reportDto.getDepartmentId()).getName());
        reportEntity.setIssueDescription(reportDto.getIssueDescription());
        reportEntity.setUpdatedAt(new Date());
        reportEntity.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        repository.save(reportEntity);

        ReportHistoryEntity history = new ReportHistoryEntity();

        history.setReportId(reportEntity.getId());
        history.setUpdatedAt(new Date());
        history.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        history.setActionType("CREATE");
        history.setNewReportData(new HashMap<>());
        history.getNewReportData().put("department", reportEntity.getDepartmentName());
        history.getNewReportData().put("issueDescription", reportDto.getIssueDescription());

        reportHistoryRepository.save(history);

        return reportEntity;
    }

    public ReportEntity updateReport(String reportId, ReportDTO reportDTO) {
        ReportEntity reportEntity = null;

        if (repository != null) {
            Optional<ReportEntity> optionalReportEntity = repository.findById(reportId);

            if (optionalReportEntity.isPresent()) {
                reportEntity = optionalReportEntity.get();
            } else {
                throw new RuntimeException("Report Not Found");
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


        return reportEntity;
    }

    public byte[] exportReportsToExcel(List<String> deptNames) throws IOException {
        List<ReportEntity> reports = repository.findByDepartmentNameIn(deptNames);

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
                System.out.println("Excel file 'Reports.xlsx' created successfully!");
            } catch (IOException e)
            { e.printStackTrace();
            }
            //workbook.write(out);
            workbook.close();
            return out.toByteArray();
        }
    }

}
