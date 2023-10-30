package com.act.HR_management.Repos;

import com.act.HR_management.Models.TaxExemptionRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxExemptionRUleRepository extends JpaRepository<TaxExemptionRule,Long> {
}
