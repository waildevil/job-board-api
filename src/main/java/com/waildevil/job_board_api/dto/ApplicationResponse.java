package com.waildevil.job_board_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {
    private String resume;
    private String coverLetter;
    private LocalDateTime appliedAt;

    private EmployerSummaryDto employer;
    private JobSummaryDto job;
}
