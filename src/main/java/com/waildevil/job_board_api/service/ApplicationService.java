package com.waildevil.job_board_api.service;

import com.waildevil.job_board_api.dto.ApplicationDto;
import com.waildevil.job_board_api.entity.Application;
import com.waildevil.job_board_api.entity.Job;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.exception.ApiException;
import com.waildevil.job_board_api.mapper.ApplicationMapper;
import com.waildevil.job_board_api.repository.ApplicationRepository;
import com.waildevil.job_board_api.repository.JobRepository;
import com.waildevil.job_board_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application createApplication(ApplicationDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        Job job = jobRepository.findById(dto.getJobId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You already applied to this job.");
        }

        Application application = applicationMapper.toEntity(dto, user, job);
        application.setAppliedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public void deleteApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public List<Application> getApplicationsForEmployer(String email) {
        return applicationRepository.findByEmployerEmail(email);
    }

    public List<Application> getApplicationsForUser(String email) {
        return applicationRepository.findByUserEmail(email);
    }

    public List<Application> getApplicationsForJob(Long jobId, String employerEmail) {
        Job job = jobRepository.findById(jobId).orElseThrow();

        if (!job.getEmployer().getEmail().equals(employerEmail)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You are not the owner of this job");
        }

        return applicationRepository.findByJobId(jobId);
    }


}
