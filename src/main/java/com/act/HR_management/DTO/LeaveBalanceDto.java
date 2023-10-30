package com.act.HR_management.DTO;

import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.LeaveBalance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDto {
    private Long employeeId;

    private LeaveType leaveType;

    public LeaveBalance toEntity(){
        LeaveBalance leaveBalance1 = new LeaveBalance();
        leaveBalance1.setLeaveType(this.getLeaveType());
        return leaveBalance1;
    }
    public static LeaveBalanceDto fromEntity(LeaveBalance leaveBalance1){
        LeaveBalanceDto leaveBalanceDto = new LeaveBalanceDto();
        leaveBalanceDto.setEmployeeId(leaveBalance1.getEmployee().getEmployeeId());
        leaveBalanceDto.setLeaveType(leaveBalance1.getLeaveType());
        return leaveBalanceDto;
    }
}
