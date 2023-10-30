package com.act.HR_management.Repos;

import com.act.HR_management.Models.IncomeTaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeTaxRuleRepository extends JpaRepository<IncomeTaxRule,Long> {
}
