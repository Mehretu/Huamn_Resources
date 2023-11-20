package com.act.HR_management.DTO;

import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Payroll;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PayrolImportDto {
    private String employeeId;
    private LocalDate startDate;
    private LocalDate endDate;

    private String firstName;
    private String lastName;
    private double baseSalary;

    private double grossSalary;
    private double incomeTax;
    private double pension;
    private double employerPension;
    private double benefits;
    private double totalDeduction;
    private double netPayment;

    public static PayrolImportDto fromEntity(Payroll payroll){
        PayrolImportDto payrolImportDto = new PayrolImportDto();
        payrolImportDto.setEmployeeId(payroll.getEmployee().getEmployeeId());
        payrolImportDto.setStartDate(payroll.getPayPeriodStartDate());
        payrolImportDto.setEndDate(payroll.getPayPeriodEndDate());
        payrolImportDto.setFirstName(payroll.getEmployee().getFirstName());
        payrolImportDto.setLastName(payroll.getEmployee().getLastName());
        payrolImportDto.setGrossSalary(payroll.getGrossSalary());
        payrolImportDto.setIncomeTax(payroll.getIncomeTax());
        payrolImportDto.setPension(payroll.getPension());
        payrolImportDto.setEmployerPension(payroll.getEmployerPension());
        payrolImportDto.setBenefits(payroll.getEmployee().getBenefits());
        payrolImportDto.setTotalDeduction(payroll.getTotalDeduction());
        payrolImportDto.setNetPayment(payroll.getNetPayment());
        return payrolImportDto;
    }

    public Payroll toEntity() {
        Payroll payroll = new Payroll();
        payroll.setPension(pension);
        payroll.setEmployerPension(employerPension);
        payroll.setBaseSalary(baseSalary);
        payroll.setGrossSalary(grossSalary);
        payroll.setIncomeTax(incomeTax);
        payroll.setTotalDeduction(totalDeduction);
        payroll.setPayPeriodStartDate(startDate);
        payroll.setPayPeriodEndDate(endDate);
        payroll.setNetPayment(netPayment);
        payroll.setBenefits(benefits);

        return payroll;
    }


    public static List<PayrolImportDto> toDTOList(List<Payroll> payrolls) {
        List<PayrolImportDto> payrolImportDtoList = new ArrayList<>();

        for (Payroll payroll : payrolls) {
            PayrolImportDto payrolImportDto = fromEntity(payroll);
            payrolImportDtoList.add(payrolImportDto);
        }

        return payrolImportDtoList;
    }



}
