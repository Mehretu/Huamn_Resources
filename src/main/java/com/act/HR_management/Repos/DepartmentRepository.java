package com.act.HR_management.Repos;

import com.act.HR_management.Models.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    Optional<Department> findDepartmentByDepartmentName(String departmentName);
    @Query("SELECT d FROM Department d WHERE (:query IS NULL OR " +
            "LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "d.creationDate BETWEEN :startDate AND :endDate)")
    List<Department> search(@Param("query") String query,@Param("startDate") LocalDate startDate,
                            @Param("endDate") LocalDate endDate);




}
