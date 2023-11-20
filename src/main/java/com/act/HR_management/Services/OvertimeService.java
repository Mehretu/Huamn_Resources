package com.act.HR_management.Services;

import com.act.HR_management.DTO.OvertimeDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Overtime;
import com.act.HR_management.Models.OvertimeType;
import com.act.HR_management.Repos.EmployeeRepository;
import com.act.HR_management.Repos.OvertimeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class OvertimeService {

    private final OvertimeRepository overtimeRepository;
    private final EmployeeRepository employeeRepository;
    public OvertimeService(OvertimeRepository overtimeRepository,EmployeeRepository employeeRepository) {
        this.overtimeRepository = overtimeRepository;
        this.employeeRepository = employeeRepository;
    }

    public Overtime create(OvertimeDto overtimeDto){
        Employee employee = employeeRepository.findEmployeeByFirstNameAndLastName(overtimeDto.getEmployeeName())
                .orElseThrow(()-> new EntityNotFoundException("There is no employee found with this name"));

       Overtime overtime = overtimeDto.toEntity();
       overtime.setEmployee(employee);
       overtimeRepository.save(overtime);
       return overtime;

    }
    public List<Overtime> getOvertimeforEmployee(Long employeeID,int year, int month){
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        return overtimeRepository.findOvertimeByEmployee_IdAndOvertimeDateBetween(employeeID,startDate,endDate);
    }
    public List<Overtime> getAll(){
        List<Overtime> overtimeList = overtimeRepository.findAll();
        return overtimeList;
    }
    public Overtime update(Long id , OvertimeDto overtimeDto){
       Employee employee = employeeRepository.findEmployeeByFirstNameAndLastName(overtimeDto.getEmployeeName())
               .orElseThrow(()-> new EntityNotFoundException("There is no employee found with this name: "+overtimeDto.getEmployeeName()));
       Overtime overtime = overtimeRepository.findById(id)
               .orElseThrow(()-> new EntityNotFoundException("There is no Overtime found with this id: "+id));
       overtime.setEmployee(employee);
       overtime.setOvertimeDate(overtimeDto.getOvertimeDate());
       overtime.setOvertimeType(overtimeDto.getOvertimeType());
       overtime.setHoursWorked(overtimeDto.getHoursWorked());
       overtime.setDescription(overtimeDto.getDescription());
       return overtimeRepository.save(overtime);

    }

    public void delete(Long id){
        Overtime overtime = overtimeRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("There is no overtime found by this id: "+id));
        overtimeRepository.delete(overtime);
    }
    public double calculateOvertimePay( Employee employee,int year, int month){
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());


        List<Overtime> overtimeList = overtimeRepository
                .findOvertimeByEmployee_IdAndOvertimeDateBetween(employee.getId(), startDate,endDate);
        double totalOvertimePay = 0.0;

        for (Overtime overtime : overtimeList){
            OvertimeType overtimeType = overtime.getOvertimeType();
            double overtimeRate = overtimeType.getRate();
            double overtimHours = overtime.getHoursWorked();

            double overtimePay = overtimHours * overtimeRate;
            totalOvertimePay += overtimePay;
        }
        return totalOvertimePay;

    }
}
