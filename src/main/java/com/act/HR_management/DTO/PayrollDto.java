package com.act.HR_management.DTO;

import com.act.HR_management.Models.Payroll;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PayrollDto {

    private String employeeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double grossSalary;
    private double incomeTax;
    private double benefits;
    private double baseSalary;
    private double netPayment;
    private double pension;
    private double employerPension;
    private double totalDeduction;


    public Payroll toEntity(){
        Payroll payroll = new Payroll();
        payroll.setPayPeriodStartDate(this.startDate);
        payroll.setPayPeriodEndDate(this.endDate);
        payroll.setGrossSalary(this.grossSalary);
        payroll.setPension(this.pension);
        payroll.setBenefits(this.benefits);
        payroll.setBaseSalary(this.baseSalary);
        payroll.setTotalDeduction(this.totalDeduction);
        payroll.setEmployerPension(this.employerPension);
        payroll.setIncomeTax(this.incomeTax);
        payroll.setNetPayment(this.netPayment);
        return payroll;
    }

    public static PayrollDto fromEntity(Payroll payroll){
        PayrollDto payrollDto = new PayrollDto();
        payrollDto.setEmployeeName(payroll.getEmployee().getFirstName()+" "+payroll.getEmployee().getLastName());
        payrollDto.setStartDate(payroll.getPayPeriodStartDate());
        payrollDto.setEndDate(payroll.getPayPeriodEndDate());
        payrollDto.setGrossSalary(payroll.getGrossSalary());
        payrollDto.setNetPayment(payroll.getNetPayment());
        payrollDto.setIncomeTax(payroll.getIncomeTax());
        payrollDto.setPension(payroll.getPension());
        payrollDto.setBenefits(payroll.getBenefits());
        payrollDto.setBaseSalary(payroll.getBaseSalary());
        payrollDto.setTotalDeduction(payroll.getTotalDeduction());
        payrollDto.setEmployerPension(payroll.getEmployerPension());
        payrollDto.setEmployerPension(payroll.getEmployerPension());
        return  payrollDto;
    }
}
