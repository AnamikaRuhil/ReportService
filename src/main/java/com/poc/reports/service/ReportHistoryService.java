package com.poc.reports.service;

import com.poc.reports.dao.ReportHistoryRepository;
import com.poc.reports.models.ReportHistoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(ReportHistoryService.class);
    private final ReportHistoryRepository historyRepository;

    @Autowired
    public ReportHistoryService(ReportHistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    /**
     * find Reposrthistory based on id
     *
     * @param id
     * @return  Reposrthistory
     */
    public Optional<ReportHistoryEntity> getReportHistory(String id){
         return historyRepository.findById(id);
    }


}
