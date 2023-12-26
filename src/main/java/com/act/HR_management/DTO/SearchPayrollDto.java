package com.act.HR_management.DTO;

import lombok.Data;

@Data
public class SearchPayrollDto {
    private String employeeName;
    private int month;
    private int year;
}
