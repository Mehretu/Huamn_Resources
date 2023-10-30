    package com.act.HR_management.Conrollers;

    import com.act.HR_management.DTO.AttendanceDto;
    import com.act.HR_management.DTO.AttendanceInDto;
    import com.act.HR_management.DTO.AttendanceReportDto;
    import com.act.HR_management.DTO.AttendanceUpdateResult;
    import com.act.HR_management.Models.Attendance;
    import com.act.HR_management.Services.AttendanceService;
    import jakarta.persistence.EntityNotFoundException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/attendances")
    public class AttendanceController {
        private final AttendanceService attendanceService;

        public AttendanceController(AttendanceService attendanceService) {
            this.attendanceService = attendanceService;
        }

        @PostMapping("/create")
        @ResponseBody
        public AttendanceDto create(@RequestBody AttendanceInDto attendanceInDto){
            return attendanceService.recordAttendance(attendanceInDto);
        }

        @GetMapping("/getAttendances")
        @ResponseBody
        public List<AttendanceDto> getAttendancesByNameOrBetweenDate(@RequestParam("fullName") String fullName,
                                                                     @RequestParam("startDate") LocalDate startDate,
                                                                     @RequestParam("endDate") LocalDate endDate){
            return attendanceService.getAttendanceRecords(fullName,startDate,endDate);
        }

        @GetMapping("/department")
        public ResponseEntity<List<AttendanceDto>> getAttendanceRecordsByDepartment(
                @RequestParam Long departmentId
                ) {

            List<AttendanceDto> attendanceRecords = attendanceService.getAttendanceRecordsByDepartment(departmentId);

            return ResponseEntity.ok(attendanceRecords);
        }

        @GetMapping("/monthly")
        public ResponseEntity<List<AttendanceReportDto>> getMonthlyAttendanceReport(
                @RequestParam("year") int year,
                @RequestParam("month") int month) {
            List<AttendanceReportDto> monthlyReport = attendanceService.getMonthlyAttendanceReport(year, month);
            return new ResponseEntity<>(monthlyReport, HttpStatus.OK);
        }

        @GetMapping("/updateAbsentStatus")
        public String updateAbsentStatus() {
            attendanceService.updateAbsentStatusForEmployees();
            return "Absent status update triggered successfully.";
        }

        @PutMapping("/update/{id}")
        @ResponseBody
        public ResponseEntity<AttendanceDto> update(@RequestBody AttendanceDto attendanceDto){
            Optional<Attendance> attendance = attendanceService.getBYId(attendanceDto.getAttendanceId());
            if (attendance.isPresent()){
                attendanceDto.setAttendanceId(attendanceDto.getAttendanceId());
                return ResponseEntity.ok(attendanceService.update(attendanceDto));
            }else {
                return ResponseEntity.notFound().build();
            }
        }

        @PutMapping("/update-Attendance-List")
        public ResponseEntity<AttendanceUpdateResult> updateAttendanceList(@RequestBody List<AttendanceDto> attendanceDtoList){
            AttendanceUpdateResult result = attendanceService.updateAttendanceList(attendanceDtoList);
            return ResponseEntity.ok(result);
        }

        @DeleteMapping
        public ResponseEntity<String> deleteAttendance(@RequestParam("attendanceId") Long attendanceId){
            try {
                attendanceService.deleteAttendance(attendanceId);
                return  ResponseEntity.noContent().build();
            }catch (EntityNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Attendance with Id: "+attendanceId+"not found"+e.getMessage());
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to delete attendance with id "+attendanceId+": " + e.getMessage());
            }
        }

        @DeleteMapping("/delete-all")
        public ResponseEntity<Void> deleteAllAttendance() {
            attendanceService.deleteAll();
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/generateAbsentReport-monthly")
        public ResponseEntity<List<AttendanceReportDto>> generateAbsentReport(
                @RequestParam("year") int year,
                @RequestParam("month") int month) {

            List<AttendanceReportDto> absentReport = attendanceService.generateAbsentReport(year, month);
            return new ResponseEntity<>(absentReport, HttpStatus.OK);
        }

        @GetMapping("/absent-records")
        public List<AttendanceReportDto> getAllAbsentRecords() {
            return attendanceService.getAllAbsentRecords();
        }
    }
