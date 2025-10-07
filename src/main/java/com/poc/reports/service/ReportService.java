package com.poc.reports.service;

import com.poc.reports.dao.DepartmentRepository;
import com.poc.reports.dao.ReportHistoryRepository;
import com.poc.reports.dao.ReportRepository;
import com.poc.reports.dao.UserRepository;
import com.poc.reports.dto.ReportDTO;
import com.poc.reports.dto.ReportFilterDto;
import com.poc.reports.exception.DataNotFoundException;
import com.poc.reports.models.DepartmentEntity;
import com.poc.reports.models.ReportEntity;
import com.poc.reports.models.ReportHistoryEntity;
import com.poc.reports.models.UserEntity;
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
import java.rmi.ServerException;
import java.util.*;

@Service
@NoArgsConstructor(force = true)
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final ReportRepository repository;

    private final DepartmentService departmentService;

    public final ReportHistoryRepository reportHistoryRepository;

    public final DepartmentRepository deptRepository;

    public final UserRepository userRepository;

    @Autowired
    public ReportService(DepartmentService departmentService, ReportRepository repository, ReportHistoryRepository reportHistoryRepository, DepartmentRepository deptRepository, UserRepository userRepository) {
        this.departmentService = departmentService;
        this.repository = repository;
        this.reportHistoryRepository = reportHistoryRepository;
        this.deptRepository = deptRepository;
        this.userRepository = userRepository;
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
        Optional<DepartmentEntity> dept = deptRepository.findById(reportDto.getDepartmentId());
        if (dept.isPresent()) {
            DepartmentEntity departmentEntity = dept.get();

            reportEntity.setDepartmentName(departmentEntity.getName());
        } else {
            logger.warn("No Department found  during report creation with department id::" + reportDto.getDepartmentId());
            throw new DataNotFoundException("No Department found with department id ::" + reportDto.getDepartmentId());
        }
        reportEntity.setIssueDescription(reportDto.getIssueDescription());
        reportEntity.setCreatedAt(new Date());
        reportEntity.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        reportEntity.setUpdatedAt(new Date());
        reportEntity.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        repository.save(reportEntity);
        logger.debug("ReportService: createReport() ends. Report saved to database." + reportEntity.toString());


        ReportHistoryEntity history = new ReportHistoryEntity();

        history.setReportId(reportEntity.getId());
        history.setUpdatedAt(new Date());
        history.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        history.setActionType("CREATE");
        history.setNewReportData(new HashMap<>());
        history.getNewReportData().put("department", reportEntity.getDepartmentName());
        history.getNewReportData().put("issueDescription", reportEntity.getIssueDescription());

        reportHistoryRepository.save(history);
        logger.debug("ReportService: createReport() ends. ReportHistory saved to database." + history.toString());
        return reportEntity;
    }

    /**
     * Updates existing report and creates report history for updated report details.
     *
     * @param reportDTO Report object to be updated
     * @param reportId  Id of report
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
        Optional<DepartmentEntity> dept = deptRepository.findById(reportDTO.getDepartmentId());
        if (dept.isPresent()) {
            DepartmentEntity departmentEntity = dept.get();

            String newDepartmentName = departmentEntity.getName();
            if (!reportEntity.getDepartmentName().equals(newDepartmentName)) {
                history.getOldReportData().put("department", reportEntity.getDepartmentName());
                history.getNewReportData().put("department", newDepartmentName);
                reportEntity.setDepartmentName(newDepartmentName);
            }
        } else {
            logger.warn("No Department found  during report update with department id::" + reportDTO.getDepartmentId());
            throw new DataNotFoundException("No Department found with department id ::" + reportDTO.getDepartmentId());
        }

        if (!reportEntity.getIssueDescription().equals(reportDTO.getIssueDescription())) {
            history.getOldReportData().put("issueDescription", reportEntity.getIssueDescription());
            history.getNewReportData().put("issueDescription", reportDTO.getIssueDescription());
        }

        reportEntity.setIssueDescription(reportDTO.getIssueDescription());

        reportEntity.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        reportEntity.setUpdatedAt(new Date());
        repository.save(reportEntity);
        logger.debug("ReportService: updateReport() ends. Report updated successfully::{} ", reportEntity.toString());
        reportHistoryRepository.save(history);
        logger.debug("ReportService: updateReport() ends.ReportHistory updated successfully::{} ", history.toString());
        return reportEntity;
    }

    /**
     * Exports reports for the given list of department names to an Excel file.
     * </p>Method fetches all reports corresponding to the provided department names,
     * and  generates an Excel workbook containing the report data, and returns it as a byte array.</p>
     *
     * @param deptNames List of department names for which reports need to be exported
     * @return export status representing report export is success/fail
     * @throws IOException If an error occurs while creating or writing the Excel file
     */
    public String exportReportsToExcel(List<String> deptNames) throws IOException {
        List<ReportEntity> reports = repository.findByDepartmentNameIgnoreCaseIn(deptNames);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reports");

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THICK);
            headerStyle.setBorderTop(BorderStyle.THICK);
            headerStyle.setBorderRight(BorderStyle.THICK);
            headerStyle.setBorderLeft(BorderStyle.THICK);

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Department Name", "Issue Description"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (ReportEntity report : reports) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(report.getId());
                row.createCell(1).setCellValue(report.getDepartmentName());
                row.createCell(2).setCellValue(report.getIssueDescription());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream("Reports.xlsx")) {
                workbook.write(fileOut);
                //workbook.write(out);
                if (sheet.getPhysicalNumberOfRows() > 1){
                    return("Report export success!!!!!");
                }
            } catch (IOException e) {
                logger.error("error while report export to excel");
                throw new RuntimeException("Failed to export Excel report",e);
            }
            return "Export success, but file is empty";
        }
    }

    /**
     * Method returns list of reports based on reportId and DateOfCreation of Report
     *
     * @param id       reportId for filtering
     * @param fromDate start date for filtering
     * @param toDate   end date for filtering
     * @return list of reports filtered on id and date
     */
    public List<ReportEntity> getReports(String id, Date fromDate, Date toDate) {
        if (toDate == null) {
            toDate = new Date();
        }
        if (fromDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
            fromDate = cal.getTime();
        }
        if (id != null) {
            return repository.findById(id)
                    .map(List::of)
                    .orElseThrow(() -> new DataNotFoundException(id));
        }
        List<ReportEntity> reports = repository.findByCreatedAtBetween(fromDate, toDate);
        if (reports.isEmpty()) {
            throw new DataNotFoundException("No reports found in date range: " + fromDate + " to " + toDate);
        }
        return reports;
    }

    public List<ReportEntity> getReportsBasedOnRole(String username) {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            String role = user.get().getRoleEntity().getRole();
            if (role.equals("ADMIN")) {
                return repository.findAll();
            } else if (role.equals("MANAGER")) {
                return repository.findByDepartmentNameIgnoreCase(user.get().getDepartment().getName());
            } else {
                return repository.findByCreatedBy(user.get().getUsername());
            }

        } else {
            throw new DataNotFoundException("User Not Found for username :" + username);
        }

    }

    public List<ReportEntity> newFilter(ReportFilterDto rf) {

        switch (rf.getFieldName()) {
            case "reportId":
                Optional<ReportEntity> reo = repository.findById(rf.getReportId());
                if (reo.isPresent()) {
                    ArrayList<ReportEntity> ret = new ArrayList<>();
                    ret.add(reo.get());
                    return ret;
                }
                break;
            case "createdOn":
                if (rf.getCreatedFrom() == null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(1900, Calendar.JANUARY, 1, 0, 0, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    Date minDate = calendar.getTime();
                    rf.setCreatedFrom(minDate);
                }
                if (rf.getCreatedTo() == null) {
                    rf.setCreatedTo(new Date());
                }
                return repository.findByCreatedAtBetween(rf.getCreatedFrom(), rf.getCreatedTo());

            case "department":
                return repository.findByDepartmentNameIgnoreCase(rf.getDeptName());

            case "issueDescription":
                return repository.findByIssueDescriptionRegex(rf.getIssuePattern());

            default:
                return repository.findAll();

        }

        return null;
    }

}
