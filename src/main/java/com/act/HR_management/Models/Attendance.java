package com.act.HR_management.Models;

import com.act.HR_management.Models.Enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private LocalDate recordDate;

    private LocalDateTime inTime;

    private LocalDateTime outTime;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;



    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;
}
