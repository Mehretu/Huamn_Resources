package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.PayrolImportDto;
import com.act.HR_management.DTO.PayrollDto;
import com.act.HR_management.DTO.PayrollRequestDto;
import com.act.HR_management.DTO.PayrollResponseDto;
import com.act.HR_management.Models.Payroll;
import com.act.HR_management.Services.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payroll")
public class PayrollController {
    private static final Logger logger = LoggerFactory.getLogger(PayrollController.class);

    private final PayrollService payrollService;



        public PayrollController(PayrollService payrollService) {
            this.payrollService = payrollService;
        }

        @PostMapping("/create")
        public ResponseEntity<PayrollResponseDto> createPayroll(@RequestBody PayrollRequestDto requestDto) {
            PayrollResponseDto payrollResponseDto = payrollService.create(requestDto);
            return new ResponseEntity<>(payrollResponseDto, HttpStatus.CREATED);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Payroll> getPayrollById(@PathVariable Long id) {
            Payroll payroll = payrollService.getPayrollById(id);
            return new ResponseEntity<>(payroll, HttpStatus.OK);
        }

        @GetMapping("/all")
        public ResponseEntity<List<Payroll>> getAllPayrolls() {
            List<Payroll> payrolls = payrollService.getAllPayrolls();
            return new ResponseEntity<>(payrolls, HttpStatus.OK);
        }



        @PutMapping("/{id}")
        public ResponseEntity<PayrollResponseDto> updatePayroll(
                @PathVariable Long id,
                @RequestBody PayrollDto payrollDto
        ) {
            PayrollResponseDto updatedPayroll = payrollService.update(id, payrollDto);
            return new ResponseEntity<>(updatedPayroll, HttpStatus.OK);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
            payrollService.deletePayroll(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @GetMapping("/search")
        public ResponseEntity<List<PayrollResponseDto>> searchPayrolls(
                @RequestParam String employeeName,
                @RequestParam int year,
                @RequestParam int month
        ) {
            List<PayrollResponseDto> payrolls = payrollService.search(employeeName, year, month);
            return new ResponseEntity<>(payrolls, HttpStatus.OK);
        }

@GetMapping("/exportTemplate")
    public ResponseEntity<byte[]> exportPayrollHeaders() {
        try {
            byte[] excelBytes = payrollService.exportPayrollHeadersToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "payroll_headers.xlsx");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}

