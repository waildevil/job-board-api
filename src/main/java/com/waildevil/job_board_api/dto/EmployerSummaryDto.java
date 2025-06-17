package com.waildevil.job_board_api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerSummaryDto {
    private Long id;
    private String name;
    private String email;
}

