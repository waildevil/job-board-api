package com.waildevil.job_board_api.service;

import com.waildevil.job_board_api.dto.JobDto;
import com.waildevil.job_board_api.entity.Job;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.exception.ApiException;
import com.waildevil.job_board_api.mapper.JobMapper;
import com.waildevil.job_board_api.repository.JobRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }


    public Job updateJob(Long id, JobDto dto, User authenticatedEmployer) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        if (!job.getEmployer().getId().equals(authenticatedEmployer.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can only update your own jobs.");
        }

        jobMapper.updateEntity(job, dto);
        return jobRepository.save(job);
    }


    public void deleteJob(Long id, User authenticatedEmployer) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        if (!job.getEmployer().getId().equals(authenticatedEmployer.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can only delete your own jobs.");
        }

        jobRepository.delete(job);
    }

    public List<Job> getJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    public List<Job> searchJobs(String title, String location, String type, Integer minSalary, Integer maxSalary) {
        return jobRepository.searchJobs(title, location, type, minSalary, maxSalary);
    }


    public Page<Job> getJobsPaginated(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }



}
