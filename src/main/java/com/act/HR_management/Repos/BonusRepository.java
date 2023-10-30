package com.act.HR_management.Repos;

import com.act.HR_management.Models.Bonus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonusRepository extends JpaRepository<Bonus, Long> {

    List<Bonus> findBonusesByEmployee_EmployeeId(Long id);
}
