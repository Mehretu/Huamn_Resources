package com.act.HR_management.Utility;

import com.act.HR_management.DTO.EmployeeDto;
import lombok.Getter;

@Getter
public enum EmployeeEnum {


    EMPLOYEE_ENUM_1("EM1","Mehretu", "Abraham","Male","0964019942","mehertu.abreham@gmail.com","Staff","Teacher",25000,2000),
    EMPLOYEE_ENUM_2("EM2","Mastewal", "Abegaz","Male","0964019944","mast.ab@gmail.com","Management","Director",30000,4000),
    EMPLOYEE_ENUM_3("EM3","Aman", "Debebe","Male","0964034942","aman.debebe@gmail.com","Staff","Teacher",15000,1000);



    private final String employeeId;

    private final String firstName;

    private final String lastName;

    private final String gender;

    private final String phone;

    private final String email;

    private final String departmentName;

    private final String jobPosition;

    private final double baseSalary;

    private final double benefits;

    EmployeeEnum(String employeeId, String firstName, String lastName, String gender, String phone, String email, String departmentName, String jobPosition, double baseSalary, double benefits){

        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName =lastName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.departmentName = departmentName;
        this.jobPosition = jobPosition;
        this.baseSalary = baseSalary;
        this.benefits = benefits;
    }




}
