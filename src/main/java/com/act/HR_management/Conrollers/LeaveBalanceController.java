package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.LeaveBalanceDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.LeaveBalance;
import com.act.HR_management.Services.EmployeeService;
import com.act.HR_management.Services.LeaveBalanceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaveBalance")
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;
    private final EmployeeService employeeService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService,EmployeeService employeeService) {
        this.leaveBalanceService = leaveBalanceService;
        this.employeeService = employeeService;
    }
    @PostMapping("/create")
    public LeaveBalance create(@RequestParam("employeeId") Long employeeId,
                               @RequestParam("leaveType") LeaveType leaveType){

        LeaveBalance leaveBalance = leaveBalanceService.createLeaveBalance(employeeId,leaveType);
        return leaveBalance;
    }

    @GetMapping("/get")
    public LeaveBalance getLeaveBalance(@RequestParam Long employeeId, @RequestParam LeaveType leaveType){
        Employee employee = employeeService.getById(employeeId)
                .orElseThrow(()-> new EntityNotFoundException("There is no Employee Saved with this id:" + employeeId));

        return leaveBalanceService.getLeaveBalance(employee,leaveType);
    }
    @PutMapping("/update")
    public LeaveBalance updateLeaveBalance(
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("leaveType") LeaveType leaveType,
            @RequestParam("newBalance") double newBalance) {


        return leaveBalanceService.updateLeaveBalance(employeeId, leaveType, newBalance);
    }

}
