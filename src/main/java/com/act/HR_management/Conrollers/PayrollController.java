package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.*;
import com.act.HR_management.Exception.ExceptionResponse;
import com.act.HR_management.Models.Payroll;
import com.act.HR_management.Repos.PayrollRepository;
import com.act.HR_management.Services.PayrollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
    private static final Logger logger = LoggerFactory.getLogger(PayrollController.class);

    private final PayrollService payrollService;
    private final PayrollRepository payrollRepository;



        public PayrollController(PayrollService payrollService, PayrollRepository payrollRepository) {
            this.payrollService = payrollService;
            this.payrollRepository = payrollRepository;
        }

        @PostMapping("/add")
        public ResponseEntity<PayrollResponseDto> createPayroll(@RequestBody PayrollRequestDto requestDto) {
            PayrollResponseDto payrollResponseDto = payrollService.create(requestDto);
            return new ResponseEntity<>(payrollResponseDto, HttpStatus.CREATED);
        }

        @GetMapping("/get/{id}")
        public ResponseEntity<Payroll> getPayrollById(@PathVariable Long id) {
            Payroll payroll = payrollService.getPayrollById(id);
            return new ResponseEntity<>(payroll, HttpStatus.OK);
        }

        @GetMapping("/getAll")
        public ResponseEntity<Object> getAllPayrolls(
                @RequestParam(name = "query", required = false) String query,
                @RequestParam(name = "startDate", required = false) LocalDate startDate,
                @RequestParam(name = "endDate", required = false) LocalDate endDate,
                @RequestParam(name = "department", required = false) String department,
                @RequestParam(name = "jobPosition", required = false) String jobPosition,
                @PageableDefault Pageable pageable
                ) {
            try {
                Page<Payroll> payrollPage = payrollService.getAllPayrolls(query, startDate, endDate, department, jobPosition, pageable);
                List<Payroll> payrolls = payrollPage.getContent();
                Map<String,Object> result = new HashMap<>();
                result.put("data", payrolls);
                result.put("total", payrollPage.getTotalElements());
                return ResponseEntity.ok(result);
            }catch (Exception e){
                ExceptionResponse err = new ExceptionResponse("Error fetching payrolls: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
            }
        }
        @GetMapping("/findAll")
        public ResponseEntity<?> findAll(
                @RequestParam(name = "searchQuery", required = false) String searchQuery,
                @RequestParam(name = "department", required = false) String department,
                @RequestParam(name = "jobPosition", required = false) String jobPosition,
                @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
        ){

            List<Payroll> payrolls;
            if (searchQuery == null && department == null && jobPosition == null && startDate == null && endDate == null){
                 payrolls = payrollRepository.findAll();
            }else {
                payrolls = payrollRepository.findAllByParameters(searchQuery,department,jobPosition,startDate,endDate);
            }
            if (payrolls.isEmpty()){
                return  ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(payrolls);
//                    = payrollService.findAll(searchQuery,department,jobPosition,startDate,endDate);
//            return ResponseEntity.ok(payrolls);
        }

        @PutMapping("/update/{id}")
        public ResponseEntity<PayrollResponseDto> updatePayroll(
                @PathVariable Long id,
                @RequestBody PayrollDto payrollDto
        ) {
            PayrollResponseDto updatedPayroll = payrollService.update(id, payrollDto);
            return new ResponseEntity<>(updatedPayroll, HttpStatus.OK);
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deletePayroll(@PathVariable Long id) {
            payrollService.deletePayroll(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        @GetMapping("/search")
        public ResponseEntity<List<PayrollResponseDto>> searchPayrolls(
                @RequestParam("employeeName") String employeeName,
                @RequestParam("year") int year,
                @RequestParam("month") int month
                ) {
            List<PayrollResponseDto> payrolls = payrollService.search(employeeName,year,month);
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

