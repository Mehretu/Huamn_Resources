package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.LeaveDto;
import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.Leave;
import com.act.HR_management.Services.LeaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {
    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/create")
    public ResponseEntity<Leave> create(@RequestParam("leaveRequestId") Long leaveRequestId){
        Leave createdLeave = leaveService.createLeave(leaveRequestId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLeave);
    }

    @GetMapping("/getById/{leaveId}")
    public ResponseEntity<LeaveDto> getById(@PathVariable Long id){
        LeaveDto leaveDto = leaveService.getLeaveById(id);
        return ResponseEntity.ok(leaveDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<LeaveDto>> getAll(){
        List<LeaveDto> leaveDtos = leaveService.getAll();
        return ResponseEntity.ok(leaveDtos);
    }

    @GetMapping("/getAllByEmployeeId/{employeeId}")
    public ResponseEntity<List<LeaveDto>> getByEmployeeId(@PathVariable Long employeeId){
        List<LeaveDto> leaveDtos = leaveService.getAllByEmployees(employeeId);
        return ResponseEntity.ok(leaveDtos);
    }

    @GetMapping("/getAllByLeaveStatus")
    public ResponseEntity<List<LeaveDto>> getByLeaveStatus(@RequestParam LeaveStatus leaveStatus){
        List<LeaveDto> leaveDtoList = leaveService.getAllByLeaveStatus(leaveStatus);
        return ResponseEntity.ok(leaveDtoList);
    }

    @PutMapping("/update/{leaveId}")
    public ResponseEntity<LeaveDto> update(@PathVariable Long leaveId,
                                           @RequestBody LeaveDto leaveDto){
        LeaveDto updated = leaveService.update(leaveId,leaveDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{leaveId}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long leaveId){
        leaveService.delete(leaveId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/cancel/{leaveId}")
    public ResponseEntity<Void>  cancelLeave(@PathVariable Long leaveId){
        leaveService.cancelLeave(leaveId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<LeaveDto>> search(
            @RequestParam(name = "employeeName", required = false) String employeeName,
            @RequestParam(name = "leaveType", required = false) LeaveType leaveType,
            @RequestParam(name = "startDate", required = false) LocalDate startDate,
            @RequestParam(name = "endDate", required = false) LocalDate endDate
    ){
        List<LeaveDto> leaveDtos = leaveService.search(employeeName, leaveType, startDate, endDate);
        return ResponseEntity.ok(leaveDtos);
    }
}
