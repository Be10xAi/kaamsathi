package com.kaamsaathi.repository;

import com.kaamsaathi.model.EmployerJobRequest;
import com.kaamsaathi.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployerRequestRepository extends JpaRepository<EmployerJobRequest, Long> {
    List<EmployerJobRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
}
