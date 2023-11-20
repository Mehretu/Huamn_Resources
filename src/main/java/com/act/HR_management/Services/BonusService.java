package com.act.HR_management.Services;

import com.act.HR_management.DTO.BonusDto;
import com.act.HR_management.Models.Bonus;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Repos.BonusRepository;
import com.act.HR_management.Repos.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class BonusService {

    private final BonusRepository bonusRepository;
    private final EmployeeRepository employeeRepository;


    public BonusService(BonusRepository bonusRepository,EmployeeRepository employeeRepository) {
        this.bonusRepository = bonusRepository;
        this.employeeRepository = employeeRepository;
    }

    public Bonus create(BonusDto bonusDto){
        Employee employee = employeeRepository.findEmployeeByFirstNameAndLastName(bonusDto.getEmployeeName())
                .orElseThrow(()-> new EntityNotFoundException("There is no employee found with this Name"));
        Bonus bonus = bonusDto.toEntity();
        bonus.setEmployee(employee);
        bonusRepository.save(bonus);
        return bonus;


    }
    public List<Bonus> getTotalBonuseforEmployees(Long employeeId){
        List<Bonus> bonuses = bonusRepository.findBonusesByEmployee_Id(employeeId);
        return  bonuses;
    }



    public double calalculateBonuses(Employee employee, int year,int month){
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        List<Bonus> bonuses = bonusRepository.findBonusesByEmployee_IdAndBonusDateBetween(employee.getId(),startDate,endDate);
        double totalBonuses = 0.0;

        for (Bonus bonus : bonuses){
            totalBonuses += bonus.getBonusAmount();
        }
        return totalBonuses;
    }
}
