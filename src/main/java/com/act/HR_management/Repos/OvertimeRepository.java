package com.act.HR_management.Repos;

import com.act.HR_management.Models.Overtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OvertimeRepository extends JpaRepository<Overtime, Long> {

    List<Overtime> findOvertimeByEmployee_IdAndOvertimeDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);


}
