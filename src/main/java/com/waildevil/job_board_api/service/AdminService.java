package com.waildevil.job_board_api.service;

import com.waildevil.job_board_api.dto.AdminStatsResponse;
import com.waildevil.job_board_api.repository.ApplicationRepository;
import com.waildevil.job_board_api.repository.JobRepository;
import com.waildevil.job_board_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public AdminStatsResponse getStats() {
        return AdminStatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalJobs(jobRepository.count())
                .totalApplications(applicationRepository.count())
                .build();
    }
}
