package com.act.HR_management.Repos;

import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT p FROM Payroll p " +
            "WHERE (:query IS NULL " +
            "OR LOWER(p.employee.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.employee.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.employee.employeeId) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.employee.department.departmentName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.employee.jobPosition) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.employee.phoneNumber) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:startDate IS NULL OR p.payPeriodStartDate >= :startDate) " +
            "AND (:endDate IS NULL OR p.payPeriodEndDate <= :endDate) " +
            "AND (:department IS NULL OR p.employee.department.departmentName = :department) " +
            "AND (:jobPosition IS NULL OR p.employee.jobPosition = :jobPosition) " +
            "ORDER BY p.payPeriodStartDate")
    Page<Payroll> findAllE(
            @Param("query") String query,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("department") String department,
            @Param("jobPosition") String jobPosition,
            Pageable pageable);

    @Query("SELECT p FROM Payroll p " +
            "JOIN p.employee e " +
            "JOIN e.department d " +
            "WHERE ( " +
            "   :searchQuery is null OR " +
            "   LOWER(e.firstName) LIKE %:searchQuery% OR " +
            "   LOWER(e.lastName) LIKE %:searchQuery% OR " +
            "   LOWER(d.departmentName) LIKE %:searchQuery% OR " +
            "   LOWER(e.jobPosition) LIKE %:searchQuery% " +
            ") " +
            "AND (LOWER(:departmentName) IS NULL OR LOWER(d.departmentName) = COALESCE(LOWER(:departmentName), LOWER(d.departmentName))) " +
            "AND (LOWER(:jobPosition) IS NULL OR LOWER(e.jobPosition) = COALESCE(LOWER(:jobPosition), LOWER(e.jobPosition))) " +
            "AND (:payPeriodStartDate IS NULL OR p.payPeriodStartDate >= CAST(:payPeriodStartDate AS DATE ) ) " +
            "AND (:payPeriodEndDate IS NULL OR p.payPeriodEndDate <= CAST(:payPeriodEndDate AS DATE ) )")
    Page<Payroll> findAll(
            @Param("searchQuery") String searchQuery,
            @Param("departmentName") String departmentName,
            @Param("jobPosition") String jobPosition,
            @Param("payPeriodStartDate") LocalDate payPeriodStartDate,
            @Param("payPeriodEndDate") LocalDate payPeriodEndDate,
            Pageable pageable);

    @Query("SELECT p FROM Payroll p " +
            "JOIN p.employee e " +
            "JOIN e.department d " +
            "WHERE (:searchQuery IS NULL OR e.firstName LIKE %:searchQuery% OR e.lastName LIKE %:searchQuery%) " +
            "AND (:department IS NULL OR d.departmentName = :department) " +
            "AND (:jobPosition IS NULL OR e.jobPosition = :jobPosition) " +
            "AND (:startDate IS NULL OR p.payPeriodStartDate >= cast(:startDate as date ) ) " +
            "AND (:endDate IS NULL OR p.payPeriodEndDate <= cast(:endDate as date ) )")
    List<Payroll> findAllByParameters(
            @Param("searchQuery") String searchQuery,
            @Param("department") String department,
            @Param("jobPosition") String jobPosition,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Payroll> findAllByEmployee(Employee employee);
}
