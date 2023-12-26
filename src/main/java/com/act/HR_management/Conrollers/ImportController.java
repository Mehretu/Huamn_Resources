package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.AttendanceReportDto;
import com.act.HR_management.DTO.PayrolImportDto;
import com.act.HR_management.Services.AttendanceService;
import com.act.HR_management.Services.PayrollService;
import com.act.HR_management.Utility.ExcelImport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ImportController {
    private final AttendanceService attendanceService;

    private final PayrollService payrollService;

    public ImportController(AttendanceService attendanceService, PayrollService payrollService) {
        this.attendanceService = attendanceService;
        this.payrollService = payrollService;
    }

        @PostMapping(value = "/attendance", consumes = {"multipart/form-data"})
        public ResponseEntity<?> importAttendanceData(@RequestParam("file") MultipartFile file) {
            try {
                List<AttendanceReportDto> attendanceList = ExcelImport.processExcelFile(file, AttendanceReportDto.class);
                attendanceService.saveAttendanceList(attendanceList);

                return ResponseEntity.ok("Attendance data imported successfully");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import attendance data");
            }
        }

        @PostMapping(value = "/payroll",consumes = {"multipart/form-data"})
        public ResponseEntity<?> importPayrollData(@RequestParam("file") MultipartFile file) {
            try {
                List<PayrolImportDto> payrollList = ExcelImport.processExcelFile(file, PayrolImportDto.class);
                payrollService.savePayrollList(payrollList);
                return ResponseEntity.ok("Payroll data imported successfully");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import payroll data");
            }
        }
}
