package com.waildevil.job_board_api.mapper;

import com.waildevil.job_board_api.dto.CompanyDto;
import com.waildevil.job_board_api.entity.Company;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

    private final ModelMapper mapper = new ModelMapper();

    public CompanyDto toDto(Company company) {
        return mapper.map(company, CompanyDto.class);
    }

    public Company toEntity(CompanyDto dto) {
        return mapper.map(dto, Company.class);
    }

    public void updateEntity(Company company, CompanyDto dto) {
        company.setName(dto.getName());
        company.setIndustry(dto.getIndustry());
        company.setWebsite(dto.getWebsite());
        company.setDescription(dto.getDescription());
        company.setLocation(dto.getLocation());
    }
}
