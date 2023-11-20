package com.act.HR_management.DTO;

import com.act.HR_management.Models.Overtime;
import com.act.HR_management.Models.OvertimeType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OvertimeDto {
    private String employeeName;


    private LocalDate overtimeDate;
    private OvertimeType overtimeType;

    private double hoursWorked;

    private String description;

    public Overtime toEntity(){
        Overtime overtime = new Overtime();
        overtime.setOvertimeDate(this.getOvertimeDate());
        overtime.setHoursWorked(this.getHoursWorked());
        overtime.setOvertimeType(this.overtimeType);
        overtime.setDescription(this.getDescription());
        return overtime;
    }
}
