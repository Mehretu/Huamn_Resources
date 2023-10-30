package com.act.HR_management.DTO;

import com.act.HR_management.Models.Attendance;
import com.act.HR_management.Models.Enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class AttendanceReportDto {
    private String employeeName;
    private String departmentName;
    private LocalDate date;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private AttendanceStatus attendanceStatus;

    public static AttendanceReportDto fromEntity(Attendance attendance){
        AttendanceReportDto reportDto = new AttendanceReportDto();
        reportDto.setEmployeeName(attendance.getEmployee().getFirstName()+" "+ attendance.getEmployee().getLastName());
        reportDto.setDate(attendance.getRecordDate());
        reportDto.setInTime(attendance.getInTime());
        reportDto.setOutTime(attendance.getOutTime());
        reportDto.setAttendanceStatus(attendance.getAttendanceStatus());
        reportDto.setDepartmentName(attendance.getEmployee().getDepartment().getDepartmentName());


        return reportDto;
    }
}
