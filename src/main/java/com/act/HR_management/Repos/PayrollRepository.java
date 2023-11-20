package com.act.HR_management.Repos;

import com.act.HR_management.Models.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll,Long> {
    @Query("SELECT p FROM Payroll p " +
            "JOIN p.employee e " +
            "WHERE (:employeeName IS NULL OR " +
            "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%'))) " +
            "AND (:startDate IS NULL OR p.payPeriodStartDate >= :startDate) " +
            "AND (:endDate IS NULL OR p.payPeriodEndDate <= :endDate)")
    List<Payroll> searchPayrolls(@Param("employeeName") String employeeName,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);
    @Query("SELECT p FROM Payroll p WHERE p.payPeriodStartDate >= :startDate AND p.payPeriodEndDate <= :endDate")
    List<Payroll> findPayrollBetweenDates( @Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
}
