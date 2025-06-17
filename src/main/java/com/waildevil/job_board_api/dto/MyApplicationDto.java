package com.waildevil.job_board_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyApplicationDto {
    private Long applicationId;
    private String jobTitle;
    private String coverLetter;
    private String resumePath;
    private String appliedAt;
}
