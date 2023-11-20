package com.act.HR_management.DTO;

import com.act.HR_management.Models.Department;
import com.act.HR_management.Models.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeDto {
    private Long id;

    private String employeeId;

    private String firstName;

    private String lastName;

    private String gender;

    private String phone;

    private String email;

    private String departmentName;

    private String jobPosition;

    private double baseSalary;

    private double benefits;


    public static EmployeeDto toDTO(Optional<Employee> employeeOptional) {
        Employee employee = employeeOptional.get();
        return fromEntity(employee);
    }
     public static EmployeeDto fromEntity(Employee employee){
         EmployeeDto employeeDto = new EmployeeDto();
         employeeDto.setId(employee.getId());
         employeeDto.setEmployeeId(employee.getEmployeeId());
         employeeDto.setFirstName(employee.getFirstName());
         employeeDto.setLastName(employee.getLastName());
         employeeDto.setGender(employee.getGender());
         employeeDto.setPhone(employee.getPhoneNumber());
         employeeDto.setEmail(employee.getEmail());
         if (employee.getDepartment() != null) {
             employeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
         }
         employeeDto.setJobPosition(employee.getJobPosition());
         employeeDto.setBaseSalary(employee.getBaseSalary());
         employeeDto.setBenefits(employee.getBenefits());
         return employeeDto;
     }
    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setId(this.getId());
        employee.setEmployeeId(this.getEmployeeId());
        employee.setFirstName(this.firstName);
        employee.setLastName(this.lastName);
        employee.setGender(this.gender);
        employee.setPhoneNumber(this.phone);
        employee.setJobPosition(this.jobPosition);
        employee.setEmail(this.email);
        employee.setBaseSalary(this.baseSalary);
        employee.setBenefits(this.benefits);
        return employee;
    }

    public static List<Employee> toEntityList(List<EmployeeDto> employeeDtos){
         return employeeDtos.stream()
                 .map(EmployeeDto::toEntity)
                 .collect(Collectors.toList());
    }

    public static List<EmployeeDto> toDtoList(List<Employee> employees){
         return employees.stream()
                 .map(EmployeeDto::fromEntity)
                 .collect(Collectors.toList());
    }
}
