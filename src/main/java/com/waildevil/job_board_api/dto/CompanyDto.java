package com.waildevil.job_board_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

    private Long id;

    @NotBlank(message = "Company name is required")
    private String name;

    private String industry;

    private String website;

    private String description;

    private String location;
}
