package com.act.HR_management.DTO;

import lombok.Data;

@Data
public class PayrollRequestDto {
    private String employeeId;
    private int year;
    private int month;
}
