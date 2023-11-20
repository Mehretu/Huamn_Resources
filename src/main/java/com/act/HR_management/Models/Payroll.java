package com.act.HR_management.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate payPeriodStartDate;

    private LocalDate payPeriodEndDate;

    private double grossSalary;

    private double benefits;
    private double baseSalary;

    private double netPayment;

    private double incomeTax;

    private double pension;
    private double employerPension;
    private double totalDeduction;

}
