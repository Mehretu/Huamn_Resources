package com.act.HR_management.DTO;

import com.act.HR_management.Models.Attendance;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class AttendanceReportDto {
    private LocalDate date;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private String employeeName;
    private String departmentName;
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

    public Attendance toEntity(){
        Attendance attendance = new Attendance();
        if (employeeName != null){
            String[] names = employeeName.split(" ");
            if (names.length == 2){
                Employee employee = new Employee();
                employee.setFirstName(names[0]);
                employee.setLastName(names[1]);
                attendance.setEmployee(employee);
            }
        }
        attendance.setRecordDate(this.getDate());
        attendance.setInTime(this.getInTime());
        attendance.setOutTime(this.getOutTime());
        attendance.setAttendanceStatus(this.getAttendanceStatus());
        return attendance;
    }
}
