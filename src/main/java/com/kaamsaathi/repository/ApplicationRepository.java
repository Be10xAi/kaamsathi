package com.kaamsaathi.repository;

import com.kaamsaathi.model.JobApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<JobApplication, Long> {
    boolean existsByJobIdAndPhone(Long jobId, String phone);
    boolean existsByApplicationId(String applicationId);
    Optional<JobApplication> findByApplicationId(String applicationId);

    @Query("select a from JobApplication a join fetch a.job where a.userId = :userId order by a.createdAt desc")
    List<JobApplication> findByUserIdWithJob(@Param("userId") Long userId);

    @Query("select a from JobApplication a join fetch a.job order by a.createdAt desc")
    List<JobApplication> findAllWithJob(Pageable pageable);
}
