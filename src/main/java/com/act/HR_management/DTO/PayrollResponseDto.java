package com.act.HR_management.DTO;

import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Payroll;
import lombok.Data;

import java.time.LocalDate;

@Data

public class PayrollResponseDto {
    private String employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String firstName;
    private String lastname;
    private double baseSalary;
    private double grossSalary;
    private double incomeTax;
    private double pension;
    private double employerPension;
    private double benefits;
    private double totalDeduction;
    private double netPayment;



    public Payroll toEntity() {
        Payroll payroll = new Payroll();

//        Employee employee = new Employee();
//        employee.setEmployeeId(employeeId);
//        employee.setFirstName(firstName);
//        employee.setLastName(lastname);
//        employee.setBaseSalary(baseSalary);
//        employee.setBenefits(benefits);


        payroll.setPayPeriodStartDate(this.getStartDate());
        payroll.setPayPeriodEndDate(this.getEndDate());
//        payroll.setEmployee(employee);

        payroll.setBaseSalary(this.getBaseSalary());
        payroll.setGrossSalary(this.getGrossSalary());
        payroll.setIncomeTax(this.getIncomeTax());
        payroll.setPension(this.getPension());
        payroll.setEmployerPension(this.getEmployerPension());
        payroll.setBenefits(this.getBenefits());
        payroll.setTotalDeduction(this.getTotalDeduction());
        payroll.setNetPayment(this.getNetPayment());
        return payroll;
    }

    public static PayrollResponseDto fromEntity(Payroll payroll){
        PayrollResponseDto payrollDto = new PayrollResponseDto();
        payrollDto.setEmployeeId(payroll.getEmployee().getEmployeeId());
        payrollDto.setFirstName(payroll.getEmployee().getFirstName());
        payrollDto.setLastname(payroll.getEmployee().getLastName());
        payrollDto.setStartDate(payroll.getPayPeriodStartDate());
        payrollDto.setEndDate(payroll.getPayPeriodEndDate());
        payrollDto.setBaseSalary(payroll.getEmployee().getBaseSalary());
        payrollDto.setBenefits(payroll.getEmployee().getBenefits());

        payrollDto.setGrossSalary(payroll.getGrossSalary());
        payrollDto.setNetPayment(payroll.getNetPayment());


        payrollDto.setIncomeTax(payroll.getIncomeTax());
        payrollDto.setPension(payroll.getPension());
        payrollDto.setEmployerPension(payroll.getEmployerPension());
        payrollDto.setTotalDeduction(payroll.getTotalDeduction());

        return  payrollDto;
    }


}



