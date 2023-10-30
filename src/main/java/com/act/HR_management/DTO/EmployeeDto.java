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
    private Long employeeId;

    private String firstName;

    private String lastName;

    private String gender;

    private String phone;

    private String email;

    private String departmentName;


    public static EmployeeDto toDTO(Optional<Employee> employeeOptional) {
        Employee employee = employeeOptional.get();
        return fromEntity(employee);
    }
     public static EmployeeDto fromEntity(Employee employee){
         EmployeeDto employeeDto = new EmployeeDto();
         employeeDto.setEmployeeId(employee.getEmployeeId());
         employeeDto.setFirstName(employee.getFirstName());
         employeeDto.setLastName(employee.getLastName());
         employeeDto.setGender(employee.getGender());
         employeeDto.setPhone(employee.getPhoneNumber());
         employeeDto.setEmail(employee.getEmail());
         employeeDto.setDepartmentName(employee.getDepartment().getDepartmentName());
         return employeeDto;
     }
    public Employee toEntity() {
        Employee employee = new Employee();
        employee.setEmployeeId(this.employeeId);
        employee.setFirstName(this.firstName);
        employee.setLastName(this.lastName);
        employee.setGender(this.gender);
        employee.setPhoneNumber(this.phone);
        employee.setEmail(this.email);
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
