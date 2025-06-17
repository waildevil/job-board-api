package com.waildevil.job_board_api.controller;

import com.waildevil.job_board_api.dto.ApplicationDto;
import com.waildevil.job_board_api.dto.ApplicationResponse;
import com.waildevil.job_board_api.dto.ApplicationSummaryDto;
import com.waildevil.job_board_api.dto.MyApplicationDto;
import com.waildevil.job_board_api.entity.Application;
import com.waildevil.job_board_api.mapper.ApplicationMapper;
import com.waildevil.job_board_api.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Applications", description = "Endpoints for applying to jobs and application management")
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationMapper applicationMapper;

    @GetMapping
    @Operation(summary = "Get all applications", description = "Returns all applications in the system")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDto>> getAll() {
        return ResponseEntity.ok(
                applicationService.getAllApplications()
                        .stream()
                        .map(applicationMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application by ID", description = "Returns a single application by its ID")
    public ResponseEntity<ApplicationDto> getById(@PathVariable Long id) {
        return applicationService.getApplicationById(id)
                .map(applicationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Apply to a job", description = "Submit a job application with resume and cover letter")
    public ResponseEntity<ApplicationResponse> create(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("coverLetter") String coverLetter,
            @RequestParam("jobId") Long jobId,
            Authentication auth
    ) throws IOException {

        String uploadsDir = "uploads/";
        File uploadsFolder = new File(uploadsDir);
        if (!uploadsFolder.exists()) {
            uploadsFolder.mkdirs();
        }

        String filename = UUID.randomUUID() + "_" + resume.getOriginalFilename();
        Path filepath = Paths.get(uploadsDir, filename);
        Files.copy(resume.getInputStream(), filepath);

        ApplicationDto dto = ApplicationDto.builder()
                .resume(filepath.toString())
                .coverLetter(coverLetter)
                .jobId(jobId)
                .build();

        Application created = applicationService.createApplication(dto, auth.getName());

        return ResponseEntity.ok(applicationMapper.toResponse(created));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete application", description = "Deletes an application by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employer")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Get applications for current employer", description = "Returns applications for jobs posted by the authenticated employer")
    public ResponseEntity<List<ApplicationResponse>> getEmployerApplications(Authentication auth) {
        List<Application> apps = applicationService.getApplicationsForEmployer(auth.getName());
        List<ApplicationResponse> response = apps.stream()
                .map(applicationMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Get my applications", description = "Returns all applications submitted by the authenticated candidate")
    public ResponseEntity<List<MyApplicationDto>> getMyApplications(Authentication auth) {
        List<MyApplicationDto> list = applicationService.getApplicationsForUser(auth.getName())
                .stream()
                .map(applicationMapper::toMyDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/jobs/{jobId}/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    @Operation(summary = "Get applications for a job", description = "Returns all applications submitted for a specific job posted by the authenticated employer")
    public ResponseEntity<List<ApplicationSummaryDto>> getApplicationsForJob(
            @PathVariable Long jobId,
            Authentication auth
    ) {
        List<ApplicationSummaryDto> list = applicationService.getApplicationsForJob(jobId, auth.getName())
                .stream()
                .map(applicationMapper::toSummaryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }


}
