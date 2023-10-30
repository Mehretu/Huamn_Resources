package com.act.HR_management.DTO;

import com.act.HR_management.Models.Bonus;
import lombok.Data;


import java.time.LocalDate;
@Data
public class BonusDto {


    private String employeeName;

    private double bonusAmount;

    private LocalDate bonusDate;

    private String description;

    public Bonus toEntity(){
        Bonus bonus = new Bonus();
        bonus.setBonusAmount(this.getBonusAmount());
        bonus.setBonusDate(this.getBonusDate());
        bonus.setDescription(this.getDescription());
        return bonus;
    }
}
