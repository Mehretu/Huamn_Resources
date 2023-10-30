package com.act.HR_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceInDto {
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private Long employeeId;
}
