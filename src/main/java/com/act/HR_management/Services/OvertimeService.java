//package com.act.HR_management.Services;
//
//import com.act.HR_management.DTO.OvertimeDto;
//import com.act.HR_management.Models.Employee;
//import com.act.HR_management.Models.Overtime;
//import com.act.HR_management.Repos.EmployeeRepository;
//import com.act.HR_management.Repos.OvertimeRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.temporal.TemporalAdjusters;
//import java.util.List;
//
//@Service
//public class OvertimeService {
//
//    private final OvertimeRepository overtimeRepository;
//    private final EmployeeRepository employeeRepository;
//    public OvertimeService(OvertimeRepository overtimeRepository,EmployeeRepository employeeRepository) {
//        this.overtimeRepository = overtimeRepository;
//        this.employeeRepository = employeeRepository;
//    }
//
//    public Overtime create(OvertimeDto overtimeDto){
//        Employee employee = employeeRepository.findEmployeeByFirstNameAndLastName(overtimeDto.getEmployeeName())
//                .orElseThrow(()-> new EntityNotFoundException("There is no employee found with this name"));
//
//       Overtime overtime =overtimeDto.toEntity();
//       overtime.setEmployee(employee);
//       overtimeRepository.save(overtime);
//       return overtime;
//
//    }
//    public List<Overtime> getOvertimeforEmployee(Long employeeID){
//        return overtimeRepository.findByEmployee_EmployeeId(employeeID);
//    }
//    public double calculateTotalOvertime(Long employeeId){
//        List<Overtime> overtimeList = getOvertimeforEmployee(employeeId);
//        double total = 0.0;
//        for (Overtime overtime : overtimeList){
//            total += overtime.getHoursWorked();
//        }
//        return total;
//    }
//    public double calculateOvertimePay( int year, int month){
//        LocalDate startDate = LocalDate.of(year,month,1);
//        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
//
//
//        Overtime overtime =
//
//    }
//}
