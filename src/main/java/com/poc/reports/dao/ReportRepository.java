package com.poc.reports.dao;

import com.poc.reports.models.ReportEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends MongoRepository<ReportEntity, String> {

    List<ReportEntity> findByDepartmentNameIgnoreCaseIn(List<String> names);

    Optional<ReportEntity> findById(String id);

    List<ReportEntity> findByCreatedAtBetween(Date fromDate, Date toDate);
}
