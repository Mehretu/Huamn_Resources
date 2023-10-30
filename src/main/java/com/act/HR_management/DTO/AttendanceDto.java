package com.act.HR_management.DTO;

import com.act.HR_management.Models.Attendance;
import com.act.HR_management.Models.Enums.AttendanceStatus;
import com.act.HR_management.Models.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDto {
    private Long attendanceId;
    private LocalDate recordDate;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private String notes;
    private Long employeeId;
    private AttendanceStatus attendanceStatus;

    public static AttendanceDto toDTO(Optional<Attendance> optionalAttendance) {
        Attendance attendance = optionalAttendance.get();

        return fromEntity(attendance);
    }

    public static AttendanceDto fromEntity(Attendance attendance){
        AttendanceDto dto = new AttendanceDto();
        dto.setAttendanceId(attendance.getAttendanceId());
        dto.setRecordDate(attendance.getRecordDate());
        dto.setInTime(attendance.getInTime());
        dto.setOutTime(attendance.getOutTime());
        dto.setNotes(attendance.getNotes());
        dto.setAttendanceStatus(attendance.getAttendanceStatus());
        if (attendance.getEmployee() != null){
            dto.setEmployeeId(attendance.getEmployee().getEmployeeId());
        }

        return dto;
    }
    public Attendance toEntity(){
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(this.getAttendanceId());
        attendance.setRecordDate(this.getRecordDate());
        attendance.setInTime(this.getInTime());
        attendance.setOutTime(this.getOutTime());
        attendance.setNotes(this.getNotes());
        attendance.setAttendanceStatus(this.getAttendanceStatus());
        if (employeeId != null){
            Employee employee = new Employee();
            employee.setEmployeeId(employeeId);
            attendance.setEmployee(employee);
        }
        return attendance;
    }

    public static List<AttendanceDto> toDtoList(List<Attendance> attendanceList){
        return attendanceList.stream()
                .map(AttendanceDto::fromEntity)
                .collect(Collectors.toList());
    }
    public static List<Attendance> toEntityList(List<AttendanceDto> attendanceDtoList){
        return attendanceDtoList.stream()
                .map(AttendanceDto::toEntity)
                .collect(Collectors.toList());
    }
}
