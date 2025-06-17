package com.waildevil.job_board_api.controller;

import com.waildevil.job_board_api.dto.JobDto;
import com.waildevil.job_board_api.entity.Job;
import com.waildevil.job_board_api.entity.User;
import com.waildevil.job_board_api.exception.ApiException;
import com.waildevil.job_board_api.mapper.JobMapper;
import com.waildevil.job_board_api.repository.JobRepository;
import com.waildevil.job_board_api.repository.UserRepository;
import com.waildevil.job_board_api.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Jobs", description = "Job management endpoints")
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final JobMapper jobMapper;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    @GetMapping
    @Operation(summary = "Get all jobs", description = "Returns a list of all job postings")
    public ResponseEntity<Map<String, Object>> getAllJobs(Pageable pageable) {
        Page<Job> jobPage = jobService.getJobsPaginated(pageable);

        List<JobDto> jobDtos = jobPage.getContent()
                .stream()
                .map(jobMapper::toDto)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("jobs", jobDtos);
        response.put("meta", Map.of(
                "currentPage", jobPage.getNumber(),
                "totalPages", jobPage.getTotalPages(),
                "totalItems", jobPage.getTotalElements(),
                "pageSize", jobPage.getSize()
        ));

        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Get job by ID", description = "Returns the job with the given ID")
    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJob(@PathVariable Long id) {
        return jobService.getJobById(id)
                .map(jobMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new job", description = "Creates a new job posting (EMPLOYER only)")
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobDto> createJob(@Valid @RequestBody JobDto dto, Authentication auth) {

        User employer = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Employer not found"));

        Job job = jobMapper.toEntity(dto, employer);
        job.setEmployer(employer);
        Job saved = jobService.createJob(job);
        return ResponseEntity.ok(jobMapper.toDto(saved));
    }



    @Operation(summary = "Update a job", description = "Updates an existing job posting (EMPLOYER only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<JobDto> updateJob(@PathVariable Long id,
                                            @Valid @RequestBody JobDto dto,
                                            Authentication auth) {
        User employer = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Employer not found"));

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Job not found"));

        if (!job.getEmployer().getId().equals(employer.getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You are not authorized to update this job");
        }

        jobMapper.updateEntity(job, dto);
        Job updated = jobRepository.save(job);

        return ResponseEntity.ok(jobMapper.toDto(updated));
    }


    @Operation(summary = "Delete a job", description = "Deletes a job by ID (EMPLOYER only)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id, Authentication auth) {
        User employer = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Employer not found"));

        jobService.deleteJob(id, employer);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get employer's own jobs", description = "Returns jobs posted by the logged-in EMPLOYER")
    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<List<JobDto>> getMyJobs(Authentication auth) {
        User employer = userRepository.findByEmail(auth.getName()).orElseThrow();
        List<JobDto> jobs = jobService.getAllJobs().stream()
                .filter(job -> job.getEmployer().getId().equals(employer.getId()))
                .map(job -> jobMapper.toDto(job, false))
                .toList();
        return ResponseEntity.ok(jobs);
    }

    @Operation(summary = "Search jobs", description = "Search jobs by title, location, type, and salary range")
    @GetMapping("/search")
    public ResponseEntity<List<JobDto>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer minSalary,
            @RequestParam(required = false) Integer maxSalary
    ) {
        List<JobDto> results = jobService.searchJobs(title, location, type, minSalary, maxSalary)
                .stream()
                .map(jobMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }


}
