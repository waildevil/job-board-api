package com.waildevil.job_board_api.controller;

import com.waildevil.job_board_api.dto.CompanyDto;
import com.waildevil.job_board_api.mapper.CompanyMapper;
import com.waildevil.job_board_api.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Company", description = "Endpoints for company")
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @GetMapping
    public ResponseEntity<List<CompanyDto>> getAll() {
        return ResponseEntity.ok(
                companyService.getAllCompanies()
                        .stream()
                        .map(companyMapper::toDto)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDto> getById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(companyMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CompanyDto> create(@Valid @RequestBody CompanyDto dto) {
        return ResponseEntity.ok(
                companyMapper.toDto(companyService.createCompany(dto))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDto> update(@PathVariable Long id, @Valid @RequestBody CompanyDto dto) {
        return ResponseEntity.ok(
                companyMapper.toDto(companyService.updateCompany(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
