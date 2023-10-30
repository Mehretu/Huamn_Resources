package com.act.HR_management.DTO;

import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.Leave;
import com.act.HR_management.Models.LeaveRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDto {
    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus leaveStatus;
    private LeaveType leaveType;
    private String notes;


    public Leave toEntity(){

        Leave leave = new Leave();
        leave.setStartDate(this.startDate);
        leave.setEndDate(this.endDate);
        leave.setLeaveType(this.leaveType);
        leave.setNotes(this.notes);
        return leave;
    }

    public static LeaveDto fromEntity(Leave leave){
        LeaveDto leaveDto = new LeaveDto();
        leaveDto.setStartDate(leave.getStartDate());
        leaveDto.setEndDate(leave.getEndDate());
        leaveDto.setLeaveType(leave.getLeaveType());
        leaveDto.setNotes(leave.getNotes());
        leaveDto.setEmployeeName(leave.getEmployee().getFirstName()+" "+leave.getEmployee().getLastName());
        return leaveDto;
    }

}
