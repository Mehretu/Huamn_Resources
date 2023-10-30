package com.act.HR_management.Conrollers;

import com.act.HR_management.DTO.EmployeeDto;
import com.act.HR_management.Models.Employee;
import com.act.HR_management.Services.DeparmentService;
import com.act.HR_management.Services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final DeparmentService departmentService;

    public EmployeeController(EmployeeService employeeService, DeparmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
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

    @GetMapping("/getAllBYDepartmentId")
    @ResponseBody
    public List<EmployeeDto> getAllByDepartmentId(@RequestParam("departmentId") Long departmentId){
        return employeeService.getAllByDepartmentId(departmentId);
    }

    @GetMapping("/getById/{id}")
    @ResponseBody
    public ResponseEntity<EmployeeDto> getById(@PathVariable Long id){
        Optional<Employee> employee = employeeService.getById(id);
        if (employee.isPresent()){
            EmployeeDto employeeDto = EmployeeDto.toDTO(employee);
            return ResponseEntity.ok(employeeDto);
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/update")
    @ResponseBody
    public ResponseEntity<EmployeeDto> update(@RequestBody EmployeeDto employeeDto){
        EmployeeDto updatedEmployee = employeeService.update(employeeDto);
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
