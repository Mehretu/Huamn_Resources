package com.act.HR_management.Services;

import com.act.HR_management.DTO.EmployeeDto;
import com.act.HR_management.Exception.DuplicateEmailException;
import com.act.HR_management.Models.Department;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Repos.DepartmentRepository;
import com.act.HR_management.Repos.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public EmployeeDto create(EmployeeDto employeeDto){
        if (employeeRepository.existsByEmail(employeeDto.getEmail())){
            throw new DuplicateEmailException("Email Address is already in use");
        }
        Employee employeeModel = employeeDto.toEntity();
        if (employeeDto.getDepartmentName() != null){
            Department department = departmentRepository.findDepartmentByDepartmentName(employeeDto.getDepartmentName())
                    .orElseThrow(()-> new EntityNotFoundException("There is no department found with this name "+ employeeDto.getDepartmentName()));
            employeeModel.setDepartment(department);

        }

        employeeRepository.save(employeeModel);
        return EmployeeDto.fromEntity(employeeModel);

    }
    public Employee getByName(String firstName, String lastName) {
        Employee employeeOptional = employeeRepository.findEmployeeByFirstNameAndLastName(firstName, lastName);
     return employeeOptional;
    }


    public List<EmployeeDto> getAll(){
        return EmployeeDto.toDtoList(employeeRepository.findAll());
    }

    public List<EmployeeDto> getAllByDepartmentId(Long id){
        return EmployeeDto.toDtoList(employeeRepository.findAllByDepartment_Id(id));
    }

    public Optional<Employee> getById(Long id){
        return employeeRepository.findById(id);
    }

    public EmployeeDto update(Long employeeId,EmployeeDto employeeDto){
     Employee employee = employeeRepository.findById(employeeId)
             .orElseThrow(() -> new EntityNotFoundException("Employee not Found with ID:"+ employeeDto.getEmployeeId()));

             String newEmail = employeeDto.getEmail();
             if (newEmail != null && !employee.getEmail().equals(newEmail)){
                 if (employeeRepository.existsByEmail(newEmail)){
                     throw new DuplicateEmailException("Email Address is already in use");
                 }
                 employee.setEmail(newEmail);
                 employee.setFirstName(employeeDto.getFirstName());
                 employee.setLastName(employeeDto.getLastName());
                 employee.setGender(employeeDto.getGender());
                 employee.setPhoneNumber(employeeDto.getPhone());
                 employee.setDepartment(employeeDto.toEntity().getDepartment());
                 employee = employeeRepository.save(employee);

        }
             return EmployeeDto.fromEntity(employee);
    }

    public void deleteEmployee(Long id){
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID:" + id));
        employeeRepository.delete(employee);
    }

    public List<Employee> searchEmployee(String query){
        return employeeRepository.search(query);
    }

}
