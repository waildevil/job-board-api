package com.waildevil.job_board_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSummaryDto {
    private Long id;
    private String name;
    private String email;
}
