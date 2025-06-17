package com.waildevil.job_board_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationSummaryDto {
    private Long id;
    private String resume;
    private String coverLetter;
    private LocalDateTime appliedAt;
    private CandidateSummaryDto candidate;
}
