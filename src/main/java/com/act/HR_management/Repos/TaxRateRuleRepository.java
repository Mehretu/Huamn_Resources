package com.act.HR_management.Repos;

import com.act.HR_management.Models.TaxRateRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaxRateRuleRepository extends JpaRepository<TaxRateRule,Long> {



}
