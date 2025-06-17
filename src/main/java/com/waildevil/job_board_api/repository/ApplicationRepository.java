package com.waildevil.job_board_api.repository;

import com.waildevil.job_board_api.entity.Application;
import com.waildevil.job_board_api.entity.Job;
import com.waildevil.job_board_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByUserAndJob(User user, Job job);

    @Query("SELECT a FROM Application a WHERE a.job.employer.email = :email")
    List<Application> findByEmployerEmail(String email);

    List<Application> findByUserEmail(String email);

    List<Application> findByJobId(Long jobId);


}
