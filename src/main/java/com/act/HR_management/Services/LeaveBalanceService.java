package com.act.HR_management.Services;

import com.act.HR_management.DTO.LeaveBalanceDto;
import com.act.HR_management.DTO.LeaveRequestDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.LeaveBalance;
import com.act.HR_management.Models.LeaveRequest;
import com.act.HR_management.Repos.EmployeeRepository;
import com.act.HR_management.Repos.LeaveBalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveBalanceService(LeaveBalanceRepository leaveBalanceRepository,EmployeeRepository employeeRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.employeeRepository = employeeRepository;
    }
    public LeaveBalance createLeaveBalance( Long employeeId,LeaveType leaveType){
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new EntityNotFoundException("There is no Employee found with this id:"+ employeeId));
        double initialBalance = calculateInitialLeaveBalance(employee,leaveType);
        LeaveBalance leaveBalance = new LeaveBalance();
        leaveBalance.setEmployee(employee);
        leaveBalance.setLeaveType(leaveType);
        leaveBalance.setLeaveBalance(initialBalance);
        return leaveBalanceRepository.save(leaveBalance);
    }

    public LeaveBalance getLeaveBalance(Employee employee ,LeaveType leaveType){
        return leaveBalanceRepository.findLeaveBalanceByEmployeeAndLeaveType(employee,leaveType)
                .orElseThrow(()-> new EntityNotFoundException("Leave Balance not found for Employee and Leave Type"));
    }

    public LeaveBalance updateLeaveBalance(Long employeeId,LeaveType leaveType, double newBalance){
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(()-> new EntityNotFoundException("There is no Employee found with this id: "+employeeId));
        if(employee == null || leaveType == null){
            throw new IllegalArgumentException("Employee and Leave type cannot be null");
            }
        if(newBalance < 0){
            throw new IllegalArgumentException("New balance must be non negative");
        }
        LeaveBalance leaveBalance = getLeaveBalance(employee,leaveType);
        leaveBalance.setLeaveBalance(newBalance);
        return leaveBalanceRepository.save(leaveBalance);
        }

    private double calculateInitialLeaveBalance(Employee employee, LeaveType leaveType){
      int yearsOfService = calculateYearsOfService(employee);
      double initialBalance = 0.0;
      double annual= 14.0;
      double maternity = 30.0;
      double sick= 30.0;
      if (leaveType == LeaveType.ANNUAL_LEAVE ){
          if (yearsOfService == 0){
              initialBalance = annual;
          } else {
              initialBalance = annual + yearsOfService;
          }
      } else if (leaveType == LeaveType.MATERNITY_LEAVE) {
          initialBalance = maternity;

      } else if (leaveType == LeaveType.SICK_LEAVE) {
          initialBalance = sick;
      }
      return initialBalance;
    }

    private int calculateYearsOfService(Employee employee) {
        LocalDate hireDate = employee.getHireDate();
        LocalDate currentDate = LocalDate.now();

        Period period = Period.between(hireDate,currentDate);

        int yearsOfService = period.getYears();
        return yearsOfService;
    }

    public void deductLeaveBalance(LeaveRequest leaveRequest){
        Employee employee = leaveRequest.getEmployee();
        LeaveType leaveType = leaveRequest.getLeaveType();
        double requestedDuration = leaveRequest.getDuration();
        LeaveBalance leaveBalance = getLeaveBalance(employee,leaveType);
        double currentBalance = leaveBalance.getLeaveBalance();
        if(requestedDuration > currentBalance){
            throw new IllegalArgumentException("Insufficient Leave Balance");
        }
        double updatedBalance = currentBalance - requestedDuration;
        leaveBalance.setLeaveBalance(updatedBalance);
        leaveBalanceRepository.save(leaveBalance);
    }

}
