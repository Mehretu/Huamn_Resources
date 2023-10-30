package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.AttendanceReportDto;
import com.act.HR_management.Services.AttendanceService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.act.HR_management.Utility.Export.exportToPdf;

@RestController
@RequestMapping("/reports")

public class ReportController {
    private final AttendanceService attendanceService;

    public ReportController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/excel/{year}/{month}")
    public ResponseEntity<byte[]> exportMonthlyAttendanceReportAsExcel(@PathVariable int year,
                                                                       @PathVariable int month) {
        try {
            byte[] excelData = attendanceService.exportMonthlyAttendanceReportAsExcel(year, month);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=monthly_attendance_report.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(excelData.length)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(excelData);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pdf/{year}/{month}")
    public ResponseEntity<InputStreamResource> exportMonthlyAttendanceReportAsPdf(@PathVariable int year,
                                                                                  @PathVariable int month) {

        List<AttendanceReportDto> reportData = attendanceService.getMonthlyAttendanceReport(year, month);

        // Generate the PDF
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exportToPdf(reportData, outputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "MonthlyAttendanceReport.pdf");

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }
}
