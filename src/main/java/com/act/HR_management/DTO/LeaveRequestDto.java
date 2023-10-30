package com.act.HR_management.DTO;

import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.LeaveRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestDto {
    private Long emmployeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus leaveStatus;
    private LeaveType leaveType;
    private String notes;
    private double duration;

    public void calculateDuration() {
        if (startDate != null && endDate != null) {
            duration = calculateDuration(startDate, endDate);
        }
    }


    private double calculateDuration(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);

        long weekends = IntStream.iterate(0, i -> i + 1)
                .limit(days)
                .filter(i -> start.plusDays(i).getDayOfWeek() == DayOfWeek.SATURDAY || start.plusDays(i).getDayOfWeek() == DayOfWeek.SUNDAY)
                .count();

        long workingDays = days - weekends;

        return workingDays + 1;
    }

    public LeaveRequest toEntity(){
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setStartDate(this.getStartDate());
        leaveRequest.setEndDate(this.getEndDate());
        leaveRequest.setLeaveStatus(this.getLeaveStatus());
        leaveRequest.setLeaveType(this.getLeaveType());
        leaveRequest.setNotes(this.getNotes());
        leaveRequest.setDuration(this.getDuration());
        return leaveRequest;
    }
    public static LeaveRequestDto fromEntity(LeaveRequest leaveRequest){
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setEmmployeeId(leaveRequest.getEmployee().getEmployeeId());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setLeaveStatus(leaveRequest.getLeaveStatus());
        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setNotes(leaveRequest.getNotes());
        dto.setDuration(leaveRequest.getDuration());
        return dto;
    }

}
