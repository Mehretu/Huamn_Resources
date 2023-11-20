package com.act.HR_management.Services;

import com.act.HR_management.DTO.LeaveDto;
import com.act.HR_management.DTO.LeaveRequestDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.Enums.LeaveType;
import com.act.HR_management.Models.Leave;
import com.act.HR_management.Models.LeaveRequest;
import com.act.HR_management.Repos.LeaveRepository;
import com.act.HR_management.Repos.LeaveRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final LeaveBalanceService leaveBalanceService;
    private  final LeaveRequestRepository leaveRequestRepository;

    public LeaveService(LeaveRepository leaveRepository,
                        LeaveRequestService leaveRequestService,
                        LeaveBalanceService leaveBalanceService,
                        EmployeeService employeeService,
                        LeaveRequestRepository leaveRequestRepository) {
        this.leaveRepository = leaveRepository;
        this.leaveBalanceService = leaveBalanceService;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    public Leave createLeave(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("You cant find and leave request with this id"));
        if (leaveRequest.getLeaveStatus() != LeaveStatus.APPROVED) {
            throw new IllegalArgumentException("Leave request must be approved");

        }
        Leave leave = new Leave();
        leave.setStartDate(leaveRequest.getStartDate());
        leave.setEndDate(leaveRequest.getEndDate());
        leave.setEmployee(leaveRequest.getEmployee());
        leave.setLeaveStatus(LeaveStatus.CREATED);
        leave.setLeaveType(leaveRequest.getLeaveType());
        leave.setNotes(leaveRequest.getNotes());
        leave.setCreatedOn(leaveRequest.getCreatedOn());
        leave.setUpdatedOn(leaveRequest.getUpdatedOn());

        leaveBalanceService.deductLeaveBalance(leaveRequest);
        Leave saved = leaveRepository.save(leave);


        return saved;
    }

    public LeaveDto getLeaveById(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("You cant find a leave by this id: " + leaveId));
        LeaveDto leaveDto = LeaveDto.fromEntity(leave);
        return leaveDto;
    }

    public List<LeaveDto> getAll() {
        List<Leave> leaves = leaveRepository.findAll();

        List<LeaveDto> dtoList = leaves.stream()
                .map(LeaveDto::fromEntity)
                .collect(Collectors.toList());
        return dtoList;
    }

    public List<LeaveDto> getAllByEmployees(Long employeeId) {
        List<Leave> leaves = leaveRepository.findAllByEmployee_Id(employeeId);
        List<LeaveDto> leaveDtoList = leaves.stream()
                .map(LeaveDto::fromEntity)
                .collect(Collectors.toList());
        return leaveDtoList;
    }

    public List<LeaveDto> getAllByLeaveStatus(LeaveStatus leaveStatus) {
        List<LeaveDto> leaves = leaveRepository.findAllByLeaveStatus(leaveStatus);
        return leaves;
    }


    public LeaveDto update(Long leaveId, LeaveDto updatedLeaveDto) {
        Leave existingLeave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("there is no leave with this id " + leaveId));
        if (existingLeave.getLeaveStatus() != LeaveStatus.CREATED) {
            throw new IllegalArgumentException("The status gotta be created to be updated");
        }
        Leave updatedLeave = updatedLeaveDto.toEntity();
        updatedLeave.setId(leaveId);

        updatedLeave = leaveRepository.save(updatedLeave);

        LeaveDto leaveDto = LeaveDto.fromEntity(updatedLeave);

        return leaveDto;
    }

    public void delete(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("there is no leave found with this id " + leaveId));
        leaveRepository.delete(leave);
    }

    public void cancelLeave(Long leaveId) {
        Leave existingLeave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("There is no leave found with this id " + leaveId));
        if (existingLeave.getLeaveStatus() != LeaveStatus.CREATED) {
            throw new IllegalArgumentException("Leave can not be cancelled because its status is not CREATED");
        }
        existingLeave.setLeaveStatus(LeaveStatus.CANCELLED);
        leaveRepository.save(existingLeave);
    }

    public List<LeaveDto> search(String employeeName, LeaveType leaveType, LocalDate startDate, LocalDate endDate) {
        List<Leave> leaves = leaveRepository.searchLeaves(employeeName, leaveType, startDate, endDate);
        List<LeaveDto> leaveDtoList = leaves.stream()
                .map(LeaveDto::fromEntity)
                .collect(Collectors.toList());
        return leaveDtoList;
    }
}