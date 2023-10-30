package com.act.HR_management.Repos;

import com.act.HR_management.Models.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Long> {
}
