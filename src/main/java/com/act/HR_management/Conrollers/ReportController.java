package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.AttendanceReportDto;
import com.act.HR_management.DTO.PayrollResponseDto;
import com.act.HR_management.Services.AttendanceService;
import com.act.HR_management.Services.PayrollService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/reports")

public class ReportController {
    private final AttendanceService attendanceService;
    private final PayrollService payrollService;

    public ReportController(AttendanceService attendanceService,PayrollService payrollService) {
        this.attendanceService = attendanceService;
        this.payrollService = payrollService;
    }



        @GetMapping("/payroll-pdf-report/{year}/{month}")
        public ResponseEntity<?> generatePayrollPdfReport(@PathVariable int year,
                                                          @PathVariable int month,
                                                          HttpServletResponse response) {
            try {

                List<PayrollResponseDto> payrollData = payrollService.getMonthlyReport(year,month);

                if (payrollData.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found for the specified date range.");
                }

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment; filename=payroll-report.pdf");

                payrollService.generatePayrollPdfReport(payrollData, response.getOutputStream());
                return ResponseEntity.ok("PDF report generated successfully.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF report.");
            }
        }
    @GetMapping("/attendance-pdf-report/{year}/{month}")
    public ResponseEntity<?> generateAttendancePdfReport(@PathVariable int year,
                                                      @PathVariable int month,
                                                      HttpServletResponse response) {
        try {

            List<AttendanceReportDto> reportData = attendanceService.getMonthlyAttendanceReport(year,month);

            if (reportData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data found for the specified date range.");
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=payroll-report.pdf");

            attendanceService.generateAttendancePdfReport(reportData, response.getOutputStream());
            return ResponseEntity.ok("PDF report generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating PDF report.");
        }
    }



    @GetMapping("/payrollreportExcel/{year}/{month}")
    public ResponseEntity<byte[]> generatePayrollReportExcel(@PathVariable int year,
                                                        @PathVariable int month) {
        try {
            List<PayrollResponseDto> payrollReportData = payrollService.getMonthlyReport(year, month);
            byte[] excelReport = payrollService.generatePayrollExcelReport(payrollReportData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "payroll_report.xlsx");
            return new ResponseEntity<>(excelReport, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/attendanceExcel/{year}/{month}")
    public ResponseEntity<byte[]> generateAttendanceReport(@PathVariable int year,
                                                           @PathVariable int month) {
        try {
            List<AttendanceReportDto> attendanceReportData = attendanceService.getMonthlyAttendanceReport(year, month);
            byte[] excelReport = attendanceService.generateAttendanceExcelReport(attendanceReportData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "attendance_report.xlsx");
            return new ResponseEntity<>(excelReport, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
