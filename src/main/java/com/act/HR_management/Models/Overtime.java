package com.act.HR_management.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Overtime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_Id")
    private Employee employee;

    private LocalDate overtimeDate;

//    private double overtimeRate;

    private double hoursWorked;

    private String description;

    @ManyToOne
    private OvertimeType overtimeType;
}
