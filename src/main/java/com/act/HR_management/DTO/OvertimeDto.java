package com.act.HR_management.DTO;

import com.act.HR_management.Models.Overtime;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OvertimeDto {
    private String employeeName;


    private LocalDate overtimeDate;

    private double hoursWorked;

    private String description;

    public Overtime toEntity(){
        Overtime overtime = new Overtime();
        overtime.setOvertimeDate(this.getOvertimeDate());
        overtime.setHoursWorked(this.getHoursWorked());
        overtime.setDescription(this.getDescription());
        return overtime;
    }
}
