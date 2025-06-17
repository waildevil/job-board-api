package com.waildevil.job_board_api.mapper;

import com.waildevil.job_board_api.dto.EmployerSummaryDto;
import com.waildevil.job_board_api.dto.JobDto;
import com.waildevil.job_board_api.entity.Job;
import com.waildevil.job_board_api.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    private final ModelMapper mapper = new ModelMapper();

    public JobDto toDto(Job job) {
        return toDto(job, true);
    }

    public JobDto toDto(Job job, boolean includeEmployer) {
        JobDto dto = JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .type(job.getType())
                .minSalary(job.getMinSalary())
                .maxSalary(job.getMaxSalary())
                .salaryText(job.getSalaryText())
                .build();

        if (includeEmployer && job.getEmployer() != null) {
            dto.setEmployer(EmployerSummaryDto.builder()
                    .id(job.getEmployer().getId())
                    .name(job.getEmployer().getName())
                    .email(job.getEmployer().getEmail())
                    .build());
        }

        return dto;
    }

    public Job toEntity(JobDto dto, User employer) {
        Job job = mapper.map(dto, Job.class);
        job.setEmployer(employer);
        job.setSalaryText(formatSalary(dto.getMinSalary(), dto.getMaxSalary()));
        return job;
    }

    public void updateEntity(Job job, JobDto dto) {
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setLocation(dto.getLocation());
        job.setType(dto.getType());
        job.setMinSalary(dto.getMinSalary());
        job.setMaxSalary(dto.getMaxSalary());
        job.setSalaryText(formatSalary(dto.getMinSalary(), dto.getMaxSalary()));
    }



    private String formatSalary(Integer min, Integer max) {
        if (min != null && max != null) {
            if (min.equals(max)) {
                return String.format("€%,d", min);
            }
            return String.format("€%,d - €%,d", min, max);
        } else if (min != null) {
            return String.format("From €%,d", min);
        } else if (max != null) {
            return String.format("Up to €%,d", max);
        }
        return null;
    }

}

