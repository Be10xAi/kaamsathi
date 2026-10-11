package com.kaamsaathi.repository;

import com.kaamsaathi.model.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    /** city = "" means "any city"; kw is already lower-cased and wrapped in % */
    @Query("""
           select j from Job j
           where j.active = true
             and (:city = '' or j.city = :city)
             and (lower(j.title) like :kw or lower(j.titleHi) like :kw
                  or lower(j.company) like :kw or lower(j.category) like :kw)
           order by j.id
           """)
    List<Job> search(@Param("city") String city, @Param("kw") String kw, Pageable pageable);
}
