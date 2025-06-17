package com.waildevil.job_board_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSummaryDto {
    private Long id;
    private String title;
    private String location;
}
