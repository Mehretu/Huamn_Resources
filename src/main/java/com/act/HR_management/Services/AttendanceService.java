package com.act.HR_management.Services;

import com.act.HR_management.DTO.AttendanceUpdateResult;
import com.act.HR_management.DTO.*;
import com.act.HR_management.Models.Attendance;
import com.act.HR_management.Models.Enums.AttendanceStatus;
import com.act.HR_management.Models.Department;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Repos.AttendanceRepository;
import com.act.HR_management.Repos.DepartmentRepository;
import com.act.HR_management.Repos.EmployeeRepository;
import com.act.HR_management.Utility.ReportGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;


    public AttendanceService(AttendanceRepository attendanceRepository,
                             EmployeeRepository employeeRepository,
                             DepartmentRepository departmentRepository
                            ) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;

    }

    public AttendanceDto recordAttendance(AttendanceInDto attendanceInDto){
        validateInAndOutTimes(attendanceInDto.getInTime(),attendanceInDto.getOutTime());
        AttendanceStatus attendanceStatus = determineAttendanceStatus(attendanceInDto.getInTime(),attendanceInDto.getOutTime());
        Employee employee = employeeRepository.findById(attendanceInDto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee is not found with id: "+attendanceInDto.getEmployeeId()));
        Attendance attendance = new Attendance();
        attendance.setInTime(attendanceInDto.getInTime());
        attendance.setOutTime(attendanceInDto.getOutTime());


        attendance.setEmployee(employee);
        attendance.setAttendanceStatus(attendanceStatus);
        attendance.setRecordDate(LocalDate.now());
        Attendance savedAttendance = attendanceRepository.save(attendance);
        return AttendanceDto.fromEntity(savedAttendance);
    }

    private void validateInAndOutTimes(LocalDateTime inTime, LocalDateTime outTime){
        if (inTime == null || outTime == null){
            throw new IllegalArgumentException("Clock-in and clock-out time is required.");
        }
        if(inTime.isAfter(outTime)){
            throw new IllegalArgumentException("Clock-out time cannot be earlier.");
        }
    }
    private void validateDate(LocalDate startDate, LocalDate endDate){
        if (startDate == null || endDate == null){
            throw new IllegalArgumentException("Clock-in and clock-out time is required.");
        }
        if(startDate.isAfter(endDate)){
            throw new IllegalArgumentException("Clock-out time cannot be earlier.");
        }
    }

    private AttendanceStatus determineAttendanceStatus(LocalDateTime inTime, LocalDateTime outTime){
        LocalTime lateArrival = LocalTime.of(8, 45);
        if (inTime.toLocalTime().isAfter(lateArrival)) {
            return AttendanceStatus.LATE;
        } else {
            return AttendanceStatus.PRESENT;
        }
    }

    public List<AttendanceDto> getAttendanceRecords(String fullName, LocalDate startDate, LocalDate endDate){

        validateDate(startDate, endDate);
        List<Attendance> attendanceList;
        if (fullName != null && !fullName.isEmpty()){

            String[] names = fullName.split("\\s+");
        if (names.length >= 1){
            String firstName = names[0];
            String lastName = (names.length >= 2) ? names[names.length - 1]: "";
            attendanceList = attendanceRepository.findByEmployee_FirstNameOrEmployee_LastNameAndRecordDateBetween(firstName,lastName,startDate,endDate);
        }else{
            throw new IllegalArgumentException("Please provide valid name");
        }
    } else {
            attendanceList = attendanceRepository.findAllByRecordDateBetween(startDate, endDate);
        }
        return AttendanceDto.toDtoList(attendanceList);

}
    public List<AttendanceDto> getAttendanceRecordsByDepartment(Long departmentId) {
       Department department = departmentRepository.findById(departmentId)
               .orElseThrow(()-> new EntityNotFoundException("Department not Found with id: "+departmentId));
       List<Attendance> attendanceList = attendanceRepository.findByEmployee_Department(department);

       return attendanceList.stream()
               .map(AttendanceDto::fromEntity)
               .collect(Collectors.toList());
    }

    public AttendanceDto update(AttendanceDto attendanceDto){
        return AttendanceDto.fromEntity(attendanceRepository.save(attendanceDto.toEntity()));
    }

    public Optional<Attendance> getBYId(Long id){
        return attendanceRepository.findById(id);
    }

    public List<AttendanceReportDto> generateAbsentReport(int year, int month){
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Attendance> absentList = attendanceRepository.findAllByRecordDateBetweenAndAttendanceStatus(startDate,endDate,AttendanceStatus.ABSENT);
        return absentList.stream()
                .map(AttendanceReportDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AttendanceReportDto> getAllAbsentRecords(){
        List<Attendance> absentList = attendanceRepository.findAllByAttendanceStatus(AttendanceStatus.ABSENT);
        return absentList.stream()
                .map(AttendanceReportDto::fromEntity)
                .collect(Collectors.toList());
    }


    @Scheduled(cron = "0 0 21 * * *")
    public void updateAbsentStatusForEmployees() {
        LocalDate targetDate = LocalDate.now().minusDays(1); // Previous day
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            List<Attendance> attendanceList = attendanceRepository.findAllByEmployeeAndRecordDate(employee, targetDate);

            if (attendanceList.isEmpty()) {
                Attendance absentAttendance = new Attendance();
                absentAttendance.setRecordDate(targetDate);
                absentAttendance.setEmployee(employee);
                absentAttendance.setAttendanceStatus(AttendanceStatus.ABSENT);

                attendanceRepository.save(absentAttendance);
            }
        }
    }

    public AttendanceUpdateResult updateAttendanceList(List<AttendanceDto> updatedAttendanceList){
        List<AttendanceDto> updatedAttendanceDtoList = updatedAttendanceList.stream()
                .map(dto -> {
                    Optional<Attendance> existingAttendance = attendanceRepository.findById(dto.getAttendanceId());
                    if (existingAttendance.isPresent()){
                        Attendance attendance = dto.toEntity();
                        attendance.setAttendanceStatus(determineAttendanceStatus(attendance.getInTime(), attendance.getOutTime()));

                        return AttendanceDto.fromEntity(attendanceRepository.save(attendance));
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new AttendanceUpdateResult(updatedAttendanceDtoList);
    }

    public List<AttendanceReportDto> saveAttendanceList(List<AttendanceReportDto> attendanceList){
        List<Attendance> attendances = attendanceList.stream()
                .map(AttendanceReportDto::toEntity)
                .collect(Collectors.toList());
        List<Attendance> saved = attendanceRepository.saveAll(attendances);
        return saved.stream()
                .map(AttendanceReportDto::fromEntity)
                .collect(Collectors.toList());
    }


    private List<String> constructAttendanceReportHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("Employee Name");
        headers.add("Department");
        headers.add("Date");
        headers.add("In Time");
        headers.add("Out Time");
        headers.add("Status");
        return headers;
    }
    public byte[] generateAttendanceExcelReport(List<AttendanceReportDto> reportData) throws IOException {
        List<String> headers = constructAttendanceReportHeaders();
        return ReportGenerator.generateExcelReport(reportData, headers);
    }

    public void generateAttendancePdfReport(List<AttendanceReportDto> reportData, OutputStream outputStream){
        List<String> headers = constructAttendanceReportHeaders();
        ReportGenerator.exportToPdf(reportData, headers, outputStream);
    }

    public List<AttendanceReportDto> getMonthlyAttendanceReport(int year, int month){
        LocalDate startDate = LocalDate.of(year,month,1);
        LocalDate endaDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        List<Attendance> attendanceList = attendanceRepository.findAllByRecordDateBetween(startDate,endaDate);
        return attendanceList.stream()
                .map(AttendanceReportDto::fromEntity)
                .collect(Collectors.toList());
    }

    public void deleteAttendance(Long id){
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Attendance not found with id: "+id));
        attendanceRepository.delete(attendance);
    }

    public void deleteAll(){
        attendanceRepository.deleteAll();
    }

}
