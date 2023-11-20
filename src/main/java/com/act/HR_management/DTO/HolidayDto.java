package com.act.HR_management.DTO;

import com.act.HR_management.Models.Holiday;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayDto {
    private String name;

    private LocalDate date;

    private String Description;

    public Holiday toEntity(){
        Holiday holiday = new Holiday();
        holiday.setName(this.getName());
        holiday.setDate(this.getDate());
        holiday.setDescription(this.getDescription());
        return holiday;
    }


}
