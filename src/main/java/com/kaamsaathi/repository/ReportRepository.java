package com.kaamsaathi.repository;

import com.kaamsaathi.model.JobReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<JobReport, Long> {
    List<JobReport> findAllByOrderByCreatedAtDesc();
}
