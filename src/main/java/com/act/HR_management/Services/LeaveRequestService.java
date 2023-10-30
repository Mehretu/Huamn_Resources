package com.act.HR_management.Services;

import com.act.HR_management.DTO.LeaveRequestDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.LeaveBalance;
import com.act.HR_management.Models.LeaveRequest;
import com.act.HR_management.Repos.EmployeeRepository;
import com.act.HR_management.Repos.LeaveRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceService leaveBalanceService;
    private final HolidayService holidayService;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, LeaveBalanceService leaveBalanceService, HolidayService holidayService,EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveBalanceService = leaveBalanceService;
        this.holidayService = holidayService;
        this.employeeRepository = employeeRepository;
    }
      public LeaveRequest create(LeaveRequestDto leaveRequestDto){
            validateLeaveRequest(leaveRequestDto);
            leaveRequestDto.calculateDuration();
            leaveRequestDto.setLeaveStatus(LeaveStatus.PENDING);
            LeaveRequest leaveRequest = leaveRequestDto.toEntity();
            if (leaveRequestDto.getEmmployeeId() != null){
                Employee employee = employeeRepository.findById(leaveRequestDto.getEmmployeeId())
                        .orElseThrow(()-> new EntityNotFoundException("There is no employee saved by this id "+leaveRequestDto.getEmmployeeId()));
                leaveRequest.setEmployee(employee);

            }
            leaveRequestRepository.save(leaveRequest);
            return leaveRequest;

      }

    private LeaveRequestDto validateLeaveRequest(LeaveRequestDto leaveRequestDto){
        Employee employee = employeeRepository.findById(leaveRequestDto.getEmmployeeId())
                .orElseThrow(()-> new EntityNotFoundException("There is no employee with this id: "+leaveRequestDto.getEmmployeeId()));
        LeaveBalance leaveBalance = leaveBalanceService.getLeaveBalance(employee,leaveRequestDto.getLeaveType());
        if (leaveBalance.getLeaveBalance() < leaveRequestDto.getDuration()){
            throw new IllegalArgumentException("Insufficient Leave Balance for the requested leave type");
        }
        adjustLeaveRequestForHolidays(leaveRequestDto);
        return leaveRequestDto;

    }

    public LeaveRequestDto adjustLeaveRequestForHolidays(LeaveRequestDto leaveRequestDto){
        LocalDate startDate = leaveRequestDto.getStartDate();
        LocalDate endDate = leaveRequestDto.getEndDate();


        List<LocalDate> holidayDates = holidayService.getAllHolidayDates();
        List<LocalDate> overlappingDates = holidayDates.stream()
                .filter(holidayDate -> !startDate.isAfter(holidayDate) && !endDate.isBefore(holidayDate))
                .collect(Collectors.toList());
        if (!overlappingDates.isEmpty()){
            long adjustedDuration = ChronoUnit.DAYS.between(startDate,endDate) + 1;
            for (LocalDate holidayDate : overlappingDates){
                if (!holidayDate.isBefore(startDate) && !holidayDate.isAfter(endDate)){
                    adjustedDuration --;
                }
            }
            leaveRequestDto.setDuration(adjustedDuration);
        }
        return leaveRequestDto;

    }
    public LeaveRequest approveLeaveRequest(Long leaveRequestId){
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new EntityNotFoundException("There is no leave request with this id"));
        if (leaveRequest.getLeaveStatus() != LeaveStatus.PENDING){
            throw new IllegalArgumentException("Leave Request status ain't right");
        }
        leaveRequest.setLeaveStatus(LeaveStatus.APPROVED);
        return leaveRequestRepository.save(leaveRequest);
    }
    public LeaveRequest rejectLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Leave request not found"));

        if (leaveRequest.getLeaveStatus() != LeaveStatus.PENDING) {
            throw new IllegalArgumentException("Leave request is not in PENDING status and cannot be rejected.");
        }
        leaveRequest.setLeaveStatus(LeaveStatus.REJECTED);
        return leaveRequestRepository.save(leaveRequest);
    }
    public Optional<LeaveRequest> getById(Long id){

        return leaveRequestRepository.findById(id);
    }

//    public LeaveRequestDto excludeWeekends(LeaveRequestDto leaveRequestDto){
//        LocalDate startDate = leaveRequestDto.getStartDate();
//        LocalDate endDate = leaveRequestDto.getEndDate();
//        LocalDate currenDate = startDate;
//        long weekends = 0;
//
//        while(!currenDate.isAfter(endDate)){
//            DayOfWeek dayOfWeek = currenDate.getDayOfWeek();
//            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY){
//                weekends++;
//            }
//            currenDate = currenDate.plusDays(1);
//        }
//        leaveRequestDto.setDuration(leaveRequestDto.getDuration() - weekends);
//        return leaveRequestDto;
//    }


}
