package com.poc.reports.dao;

import com.poc.reports.models.ReportHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportHistoryRepository extends MongoRepository<ReportHistoryEntity, Long> {
}
