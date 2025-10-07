package com.poc.reports.controller;

import com.poc.reports.exception.DataNotFoundException;
import com.poc.reports.models.ReportHistoryEntity;
import com.poc.reports.service.ReportHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reportshistory")
@Tag(name = "ReportHistory", description = " ReportHistory APIs")
public class ReportHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    private final ReportHistoryService reportHistoryService;
    @Autowired
    public ReportHistoryController(ReportHistoryService reportHistoryService) {
        this.reportHistoryService = reportHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<ReportHistoryEntity>> getReportHistory(@RequestParam String id) {
        logger.debug("Start of REQUEST: GET /reportshistory Request Param: {}",id );;

        Optional<ReportHistoryEntity> historyEntity = reportHistoryService.getReportHistory(id);
        if(historyEntity.isPresent()){
            List<ReportHistoryEntity> response= historyEntity.stream().toList();
            logger.debug("END of /reportshistory :: Response Code 200 :: Response: {}", response);
            return ResponseEntity.ok(response);
        }
        else{

            logger.debug("END of /reportshistory :: Response Code 500 :: Response: {}", "No Report History present for id :" +id);
            throw new DataNotFoundException("No Report History present for id :" +id);
        }
    }

}
