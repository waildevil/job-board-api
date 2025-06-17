package com.waildevil.job_board_api.repository;

import com.waildevil.job_board_api.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
