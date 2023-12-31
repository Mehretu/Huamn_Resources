package com.act.HR_management.Repos;

import com.act.HR_management.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    Employee findEmployeeByFirstNameAndLastName(String firstName, String lastName);


    List<Employee> findAllByDepartment_Id(@Param("id") Long id);

    boolean existsByEmail(String email);
    @Query("SELECT DISTINCT e.department.departmentName FROM Employee e")
    List<String> getDistinctDepartments();
    @Query("SELECT DISTINCT e.jobPosition FROM Employee e WHERE e.department.departmentName = :department")
    List<String> getDistinctJobPositionByDepartment(@Param("department") String department);

    @Query("SELECT e FROM Employee e WHERE (:query IS NULL OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.gender) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(e.phoneNumber) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Employee> search(String query);


    @Query("SELECT e FROM Employee e " +
            "WHERE e NOT IN (SELECT a.employee FROM Attendance a " +
            "WHERE a.recordDate BETWEEN :startDate AND :endDate)")
    List<Employee> findAbsentEmployees(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    @Query("SELECT e from Employee e WHERE " +
            "CONCAT(LOWER(e.firstName),' ', LOWER(e.lastName) ) = LOWER(:employeeName) ")
   Optional<Employee> findEmployeeByFirstNameAndLastName(@Param("employeeName") String employeeName);
 @Query("SELECT e FROM Employee e WHERE e.employeeId = :employeeId")
    Optional<Employee> findByEmployeeId(@Param("employeeId") String employeeId);
}
