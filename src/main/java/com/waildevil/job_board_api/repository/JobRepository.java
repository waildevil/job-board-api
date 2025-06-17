package com.waildevil.job_board_api.repository;

import com.waildevil.job_board_api.entity.Job;
import com.waildevil.job_board_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByEmployer(User employer);

    @Query("""
    SELECT j FROM Job j
    WHERE (:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
      AND (:type IS NULL OR LOWER(j.type) LIKE LOWER(CONCAT('%', :type, '%')))
      AND (:minSalary IS NULL OR j.minSalary >= :minSalary)
      AND (:maxSalary IS NULL OR j.maxSalary <= :maxSalary)
""")
    List<Job> searchJobs(
            @Param("title") String title,
            @Param("location") String location,
            @Param("type") String type,
            @Param("minSalary") Integer minSalary,
            @Param("maxSalary") Integer maxSalary
    );




}
