package com.act.HR_management.Services;

import com.act.HR_management.DTO.*;
import com.act.HR_management.Models.*;
import com.act.HR_management.Repos.EmployeeRepository;
import com.act.HR_management.Repos.PayrollRepository;
import com.act.HR_management.Utility.ReportGenerator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PayrollService {
        private static final Logger logger = LoggerFactory.getLogger(PayrollService.class);
        private PayrollRepository payrollRepository;
        private EmployeeService employeeService;

        private OvertimeService overtimeService;
        private BonusService bonusService;
        private TaxRateRuleService taxRateRuleService;

        private EmployeeRepository employeeRepository;


        public PayrollService(PayrollRepository payrollRepository,
                              EmployeeService employeeService,
                              OvertimeService overtimeService,
                              BonusService bonusService,
                              TaxRateRuleService taxRateRuleService,
                              EmployeeRepository employeeRepository) {
                this.payrollRepository = payrollRepository;
                this.employeeService = employeeService;
                this.overtimeService = overtimeService;
                this.bonusService = bonusService;
                this.taxRateRuleService = taxRateRuleService;
                this.employeeRepository = employeeRepository;
        }

        public PayrollResponseDto create(PayrollRequestDto requestDto){
                Employee employee = employeeRepository.findByEmployeeId(requestDto.getEmployeeId())
                        .orElseThrow(()-> new EntityNotFoundException("There is no employee found with this id: "+requestDto.getEmployeeId()));
                if (employee.getBaseSalary() == 0){
                        throw new IllegalArgumentException("At least You have to insert a base salary to create a payroll ");
                }

                PayrollResponseDto responseDto = calculatePayroll(requestDto);
                Payroll payroll =  responseDto.toEntity();
                payroll.setEmployee(employee);


                payrollRepository.save(payroll);
                return responseDto;

        }

        public List<PayrolImportDto> createList(List<PayrolImportDto> payrolImportDtoList){
                List<Payroll> payrolls = payrolImportDtoList.stream()
                        .map(PayrolImportDto::toEntity)
                        .collect(Collectors.toList());
                List<Payroll> saved = payrollRepository.saveAll(payrolls);
                return PayrolImportDto.toDTOList(saved);


        }




        public Payroll getPayrollById(Long id) {
                return payrollRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Payroll not found with id: " + id));
        }

        public Page<Payroll> getAllPayrolls(String search,
                                            LocalDate startDate,
                                            LocalDate endDate,
                                            String department,
                                            String jobPosition,
                                            Pageable pageable) {
                try {
//                        LocalDate startDate = LocalDate.of(year,month,1);
//                        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
                        Page<Payroll> payrollPage = payrollRepository.findAll(search,department,jobPosition,startDate,endDate,pageable);
                        List<Payroll> payrolls = payrollPage.getContent();
                        return new PageImpl<>(payrolls,pageable,payrollPage.getTotalElements());
                }catch (Exception e){
                        logger.error("An error occured while getting all payrolls. ", e);
                        return new PageImpl<>(Collections.emptyList(), pageable, 0);
                }
        }

       public List<Payroll> findAll(String searchQuery,
                              String department,
                              String jobPosition,
                              LocalDate startDate,
                               LocalDate endDate){
                List<Payroll> payrollList = payrollRepository.findAllByParameters(searchQuery,department,jobPosition,startDate,endDate);
                return payrollList;
        }

        public List<PayrollResponseDto> getMonthlyReport(int year, int month){
                LocalDate startDate = LocalDate.of(year,month,1);
                LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
                List<Payroll> payrollList = payrollRepository.findPayrollBetweenDates(startDate,endDate);
                return payrollList.stream()
                        .map(PayrollResponseDto::fromEntity)
                        .collect(Collectors.toList());
        }


        public PayrollResponseDto update(Long id, PayrollDto payrollDto){
                Payroll existingPayroll = payrollRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("there is no payroll found with this id " +id));
               if (existingPayroll.getPayPeriodStartDate() == null || existingPayroll.getPayPeriodEndDate() == null){
                       throw new ValidationException("Both start date and end date are required");
               }
                Payroll updated = payrollDto.toEntity();
                updated.setId(id);
                updated = payrollRepository.save(updated);
                PayrollResponseDto payrollResponseDto =  PayrollResponseDto.fromEntity(updated);
                return payrollResponseDto;
        }

        public void deletePayroll(Long id) {
                Payroll existingPayroll = getPayrollById(id);
                payrollRepository.delete(existingPayroll);
        }

        public List<PayrollResponseDto> search(String employeeName, int year, int month){
           LocalDate startDate = LocalDate.of(year,month,1);
           LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
           List<Payroll> payrolls = payrollRepository.searchPayrolls(employeeName, startDate,endDate);
           List<PayrollResponseDto> payrollResponseDtoList = payrolls.stream()
                   .map(PayrollResponseDto::fromEntity)
                   .collect(Collectors.toList());
           return payrollResponseDtoList;
        }

        private List<String> constructPayslipHeaders() {
                List<String> headers = new ArrayList<>();
                headers.add("Employee Id");
                headers.add("Start Date");
                headers.add("End Date");
                headers.add("Employee Name");
                headers.add("Base Salary");
                headers.add("Gross Salary");
                headers.add("Income Tax");
                headers.add("Pension");
                headers.add("Employer Pension");
                headers.add("Benefits");
                headers.add("Total Deduction");
                headers.add("Net Payment");
                return headers;
        }
        private List<String> constructtemplate() {
                List<String> headers = new ArrayList<>();
                headers.add("Employee Id");
                headers.add("Start Date");
                headers.add("End Date");
                headers.add("First Name");
                headers.add("Last Name");
                headers.add("Base Salary");
                headers.add("Gross Salary");
                headers.add("Income Tax");
                headers.add("Pension");
                headers.add("Employer Pension");
                headers.add("Benefits");
                headers.add("Total Deduction");
                headers.add("Net Payment");
                return headers;
        }
        public byte[] exportPayrollHeadersToExcel() throws IOException {
                List<String> headers = constructtemplate();

                try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Payroll Headers");

                        Row headerRow = sheet.createRow(0);

                        for (int i = 0; i < headers.size(); i++) {
                                Cell cell = headerRow.createCell(i);
                                cell.setCellValue(headers.get(i));
                        }


                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        workbook.write(outputStream);

                        return outputStream.toByteArray();
                }
        }
        public byte[] generatePayrollExcelReport(List<PayrollResponseDto> reportData) throws IOException {
                List<String> headers = constructPayslipHeaders();
                return ReportGenerator.generateExcelReport(reportData, headers);
        }

        public void generatePayrollPdfReport(List<PayrollResponseDto> reportData, OutputStream outputStream){
                List<String> headers = constructPayslipHeaders();
                ReportGenerator.exportToPdf(reportData,headers,outputStream);
        }




        public void savePayrollList(List<PayrolImportDto> payrolImportDtoList){
                for (PayrolImportDto dto : payrolImportDtoList){
                        try{
                                Payroll payroll = dto.toEntity();

                                if ( dto.getEmployeeId() != null){
                                        Employee employee = employeeRepository.findByEmployeeId(dto.getEmployeeId())
                                                .orElseThrow(()-> new EntityNotFoundException("Employee not found with this id: " + dto.getEmployeeId()));
                                        employee.setBaseSalary(payroll.getBaseSalary());
                                        employee.setBenefits(payroll.getBenefits());
                                        employee.setFirstName(dto.getFirstName());
                                        employee.setLastName(dto.getLastName());
                                        payroll.setEmployee(employee);
                                        payrollRepository.save(payroll);
                                }else {
                                        throw new IllegalArgumentException("Employee ID is missing or invalid for payroll: " + payroll.getId());

                                }

                        } catch (Exception e){
                                System.err.println("Error occured while saving payroll: " + e.getMessage());
                                e.printStackTrace();
                        }
                }
        }


        public PayrollResponseDto calculatePayroll(PayrollRequestDto requestDto){
                Employee employee = employeeService.findById(requestDto.getEmployeeId())
                        .orElseThrow(()-> new EntityNotFoundException("There is no Employee found with this id: "+ requestDto.getEmployeeId()));

                PayrollResponseDto responseDto = new PayrollResponseDto();
                LocalDate startDate = LocalDate.of(requestDto.getYear(),requestDto.getMonth(),1);
                LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());


                double baseSalary = employee.getBaseSalary();
                double benefits = employee.getBenefits();
                double overtimePay = overtimeService.calculateOvertimePay(employee, requestDto.getYear(), requestDto.getMonth());
                double bonuses = bonusService.calalculateBonuses(employee,requestDto.getYear(),requestDto.getMonth());

                double incomeTax = calculateIncomeTax(baseSalary);
                PensionDto pension = calculatePension(baseSalary);
                double grossSalary = baseSalary  + overtimePay + bonuses + benefits;
                double netPayment = grossSalary - incomeTax - pension.getEmployeePension();

                double employerPension = pension.getEmployerPension();
                double totalDeduction = incomeTax + pension.getEmployeePension();
                responseDto.setStartDate(startDate);
                responseDto.setEndDate(endDate);
                responseDto.setEmployeeId(employee.getEmployeeId());
                responseDto.setFirstName(employee.getFirstName());
                responseDto.setLastname(employee.getLastName());
                responseDto.setBaseSalary(baseSalary);
                responseDto.setGrossSalary(grossSalary);
                responseDto.setBenefits(benefits);
                responseDto.setIncomeTax(incomeTax);
                responseDto.setPension(pension.getEmployeePension());
                responseDto.setEmployerPension(employerPension);
                responseDto.setNetPayment(netPayment);
                responseDto.setTotalDeduction(totalDeduction);
                return responseDto;

        }

        private double calculateIncomeTax( double baseSalary) {
                double totalTax = 0.0;
                List<TaxRateRule> taxRateRules = taxRateRuleService.getAllTaxRateRules();

                for (TaxRateRule rule : taxRateRules){
                        double incomeFrom = rule.getIncomeFrom();
                        double incomeTo = rule.getIncomeTo();
                        double taxRatePercentage = rule.getTaxRatePercentage();

                        if (baseSalary >= incomeFrom){
                                if (baseSalary <= incomeTo || incomeTo == 0){
                                        totalTax += (baseSalary - incomeFrom) * (taxRatePercentage/100);
                                }else {
                                        totalTax += (incomeTo - incomeFrom) * (taxRatePercentage/100);
                                }
                        }
                }

                return totalTax;
        }

        public PensionDto calculatePension(double baseSalary){


                double employeePensionPercentage = 0.07;
                double employerPensionPercentage = 0.11;


                double employeePension = baseSalary * employeePensionPercentage;
                double employerPension = baseSalary * employerPensionPercentage;

                PensionDto pensionDto = new PensionDto();

                pensionDto.setEmployeePension(employeePension);
                pensionDto.setEmployerPension(employerPension);
                return pensionDto;
        }





}
