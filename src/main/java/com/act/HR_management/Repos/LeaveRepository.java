package com.act.HR_management.Repos;

import com.act.HR_management.DTO.LeaveDto;
import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    @Query("SELECT l from Leave l where l.employee.employeeId = :employeeId")
    List<Leave> findAllByEmployeeId(Long employeeId);

    List<LeaveDto> findAllByLeaveStatus(LeaveStatus leaveStatus);


    @Query("SELECT l FROM Leave l " +
            "JOIN l.employee e " +
            "WHERE (:employeeName IS NULL OR " +
            "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%'))) " +
            "AND (:leaveType IS NULL OR l.leaveType = :leaveType) " +
            "AND (:startDate IS NULL OR l.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR l.endDate <= :endDate)")
    List<Leave> searchLeaves(
            @Param("employeeName") String employeeName,
            @Param("leaveType") LeaveType leaveType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


}
