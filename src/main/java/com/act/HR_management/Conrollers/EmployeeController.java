package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.EmployeeDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Models.Payroll;
import com.act.HR_management.Services.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto employeeDto){
        EmployeeDto createdEmployee = employeeService.create(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @GetMapping("/getAll")
    @ResponseBody
    public List<EmployeeDto> getAll(){
        return employeeService.getAll();
    }
    @GetMapping("/allPayrolls")
    @ResponseBody
    public ResponseEntity<List<Payroll>> getEmployeePayrollByUsername(@RequestParam("id") Long id){
        List<Payroll> employeePayrolls;
        try {
            employeePayrolls = employeeService.getEmployeePayrollsByUsername(id);
            logger.info("Retrieved employee data for username: {}", id);
            return ResponseEntity.ok(employeePayrolls);
        }catch (EntityNotFoundException e){
            logger.error("Employee not found with this username: {}",id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e){
            logger.error("An error occured while retrieving employee data with this username: {}",id,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/departments")
    public ResponseEntity<Map<String,List<String>>> getDepartmentAndPositionMapping(){
        Map<String, List<String>> departmentPositionMapping = employeeService.getDepartmentAndPositionMapping();
        return ResponseEntity.ok(departmentPositionMapping);
    }

    @GetMapping("/getAllBYDepartmentId")
    @ResponseBody
    public List<EmployeeDto> getAllByDepartmentId(@RequestParam("departmentId") Long departmentId){
        return employeeService.getAllByDepartmentId(departmentId);
    }

    @GetMapping("/getById/{id}")
    @ResponseBody
    public ResponseEntity<EmployeeDto> getById( Long id){
        Optional<Employee> employee = employeeService.getById(id);
        if (employee.isPresent()){
            EmployeeDto employeeDto = EmployeeDto.toDTO(employee);
            return ResponseEntity.ok(employeeDto);
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id , @RequestBody EmployeeDto employeeDto){
        EmployeeDto updatedEmployee = employeeService.update(id,employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/search")
    public List<Employee> search(@RequestParam("query") String query){
        return employeeService.searchEmployee(query);
    }

}
