package com.act.HR_management.Repos;

import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance,Long> {
    Optional<LeaveBalance> findLeaveBalanceByEmployeeAndLeaveType(Employee employee, LeaveType leaveType);
}
