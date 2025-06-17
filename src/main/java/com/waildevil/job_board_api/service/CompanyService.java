package com.waildevil.job_board_api.service;

import com.waildevil.job_board_api.dto.CompanyDto;
import com.waildevil.job_board_api.entity.Company;
import com.waildevil.job_board_api.mapper.CompanyMapper;
import com.waildevil.job_board_api.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Company createCompany(CompanyDto dto) {
        return companyRepository.save(companyMapper.toEntity(dto));
    }

    public Company updateCompany(Long id, CompanyDto dto) {
        Company company = companyRepository.findById(id).orElseThrow();
        companyMapper.updateEntity(company, dto);
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
