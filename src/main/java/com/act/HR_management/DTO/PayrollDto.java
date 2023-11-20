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
    private double netPayment;


    public Payroll toEntity(){
        Payroll payroll = new Payroll();
        payroll.setPayPeriodStartDate(this.startDate);
        payroll.setPayPeriodEndDate(this.endDate);
        payroll.setGrossSalary(this.grossSalary);
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
        return  payrollDto;
    }
}
