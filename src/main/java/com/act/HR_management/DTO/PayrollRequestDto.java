package com.act.HR_management.DTO;

import lombok.Data;

@Data
public class PayrollRequestDto {
    private Long employeeId;
    private int year;
    private int month;
}
