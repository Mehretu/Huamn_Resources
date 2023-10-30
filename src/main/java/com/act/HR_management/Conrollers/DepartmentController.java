package com.act.HR_management.Conrollers;


import com.act.HR_management.DTO.DepartmentDto;
import com.act.HR_management.Models.Department;
import com.act.HR_management.Services.DeparmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DeparmentService deparmentService;

    public DepartmentController(DeparmentService deparmentService) {
        this.deparmentService = deparmentService;
    }

   @PostMapping("/add")
   @ResponseBody
    public DepartmentDto create(@RequestBody DepartmentDto departmentDto){
        return deparmentService.createDepartment(departmentDto);
    }

    @GetMapping("/getByDepartmentName")
    @ResponseBody
    public DepartmentDto getByDepartmentName(@RequestParam("departmentName") String departmentName){
        return deparmentService.getDepartmentByName(departmentName);
    }

    @GetMapping("/getAll")
    @ResponseBody
    public List<DepartmentDto> getAllDepartments(){
        return deparmentService.getAllDepartments();
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<DepartmentDto> getById(@PathVariable Long id){
        Optional<Department> department = deparmentService.getByDepartmentId(id);
        if (department.isPresent()){
            DepartmentDto departmentDto = DepartmentDto.toDTO(department);
            return ResponseEntity.ok(departmentDto);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<DepartmentDto> update(@PathVariable Long id,@RequestBody DepartmentDto departmentDto){
        Optional<Department> department = deparmentService.getByDepartmentId(id);
        if (department.isPresent()){
            departmentDto.setDepartmentId(id);
            return ResponseEntity.ok(deparmentService.updateDepartment(departmentDto));
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        Optional<Department> department = deparmentService.getByDepartmentId(id);
        if (department.isPresent()){
            deparmentService.delete(id);
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<DepartmentDto> search(@RequestParam("query") String query,
                                      @RequestParam("startDate")LocalDate startDate,
                                      @RequestParam("endDate") LocalDate endDate){
        return deparmentService.searchDepartments(query,startDate,endDate);
    }


}
