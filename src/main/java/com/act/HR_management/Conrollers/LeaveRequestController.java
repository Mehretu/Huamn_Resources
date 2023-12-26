package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.LeaveRequestDto;
import com.act.HR_management.Models.LeaveRequest;
import com.act.HR_management.Services.LeaveRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-request")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping("/create")
    public ResponseEntity<LeaveRequest> create(@RequestBody LeaveRequestDto leaveRequestDto){
        LeaveRequest leaveRequest = leaveRequestService.create(leaveRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveRequest);
    }
    @PutMapping("/approve/{leaveRequestId}")
    public ResponseEntity<LeaveRequest> approveLeaveRequest(@PathVariable Long leaveRequestId) {
        LeaveRequest approvedLeaveRequest = leaveRequestService.approveLeaveRequest(leaveRequestId);

        return ResponseEntity.ok(approvedLeaveRequest);
    }
    @PutMapping("/reject/{leaveRequestId}")
    public ResponseEntity<LeaveRequest> rejectLeaveRequest(@PathVariable Long leaveRequestId){
        LeaveRequest rejectedLeaveRequest = leaveRequestService.rejectLeaveRequest(leaveRequestId);
        return ResponseEntity.ok(rejectedLeaveRequest);
    }
}
