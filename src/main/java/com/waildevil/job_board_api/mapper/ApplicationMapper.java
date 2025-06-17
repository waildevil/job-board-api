package com.waildevil.job_board_api.mapper;

import com.waildevil.job_board_api.dto.*;
import com.waildevil.job_board_api.entity.Application;
import com.waildevil.job_board_api.entity.Job;
import com.waildevil.job_board_api.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    private final ModelMapper mapper = new ModelMapper();

    public ApplicationDto toDto(Application application) {
        ApplicationDto dto = mapper.map(application, ApplicationDto.class);
        dto.setUserId(application.getUser().getId());
        dto.setJobId(application.getJob().getId());
        return dto;
    }

    public Application toEntity(ApplicationDto dto, User user, Job job) {
        return Application.builder()
                .resume(dto.getResume())
                .coverLetter(dto.getCoverLetter())
                .user(user)
                .job(job)
                .build();
    }

    public ApplicationResponse toResponse(Application app) {
        return ApplicationResponse.builder()
                .resume(app.getResume())
                .coverLetter(app.getCoverLetter())
                .appliedAt(app.getAppliedAt())
                .job(JobSummaryDto.builder()
                        .id(app.getJob().getId())
                        .title(app.getJob().getTitle())
                        .location(app.getJob().getLocation())
                        .build())
                .build();
    }

    public MyApplicationDto toMyDto(Application app) {
        return MyApplicationDto.builder()
                .applicationId(app.getId())
                .jobTitle(app.getJob().getTitle())
                .coverLetter(app.getCoverLetter())
                .resumePath(app.getResume())
                .appliedAt(app.getAppliedAt().toString())
                .build();
    }

    public ApplicationSummaryDto toSummaryDto(Application app) {
        return ApplicationSummaryDto.builder()
                .id(app.getId())
                .resume(app.getResume())
                .coverLetter(app.getCoverLetter())
                .appliedAt(app.getAppliedAt())
                .candidate(CandidateSummaryDto.builder()
                        .id(app.getUser().getId())
                        .name(app.getUser().getName())
                        .email(app.getUser().getEmail())
                        .build())
                .build();
    }



}
