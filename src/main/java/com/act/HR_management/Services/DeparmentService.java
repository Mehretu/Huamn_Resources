package com.act.HR_management.Services;

import com.act.HR_management.DTO.DepartmentDto;
import com.act.HR_management.Exception.DepartmentAlreadyExistsException;
import com.act.HR_management.Models.Department;
import com.act.HR_management.Repos.DepartmentRepository;
import com.act.HR_management.Repos.EmployeeRepository;
import com.act.HR_management.Utility.DepartmentEnum;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class DeparmentService {

    private final DepartmentRepository departmentRepository;


    public DeparmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;

    }


    public DepartmentDto createDepartment(DepartmentDto departmentDto){
        String departmentName = departmentDto.getDepartmentName();
        if (StringUtils.isEmpty(departmentName)){
            throw new IllegalArgumentException("Department name cannot be empty");

        }
        if (departmentRepository.findDepartmentByDepartmentName(departmentName).isPresent()){
            throw new DepartmentAlreadyExistsException("Department with this name already exists");
        }
        Department department = departmentDto.toEntity();
        department = departmentRepository.save(department);
        return DepartmentDto.fromEntity(department);

    }

    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return DepartmentDto.toDtoList(departments);
    }

    public DepartmentDto getDepartmentByName(String departmentName){
        Department department = departmentRepository.findDepartmentByDepartmentName(departmentName)
                .orElseThrow(() -> new EntityNotFoundException("Department not Found with name: "+ departmentName));
        return DepartmentDto.fromEntity(department);
    }

    public Optional<Department> getByDepartmentId(Long id){
        return departmentRepository.findById(id);
    }

    public DepartmentDto updateDepartment(DepartmentDto departmentDto){
        return DepartmentDto.fromEntity(departmentRepository.save(departmentDto.toEntity()));
    }

    public void delete(Long id){
        Department department = departmentRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Department with this id: "+id+" doesn't exist."));
        departmentRepository.delete(department);
    }

    public List<DepartmentDto> searchDepartments(String query, LocalDate startDate,LocalDate endDate){
        List<Department> departments = departmentRepository.search(query,startDate,endDate);
        return DepartmentDto.toDtoList(departments);
    }

    public void onInit() {
        Arrays.stream(DepartmentEnum.values()).forEach(departmentEnum ->{
            DepartmentDto departmentDto = new DepartmentDto();
            departmentDto.setDepartmentName(departmentEnum.getDepartmentName());
            departmentDto.setDescription(departmentEnum.getDescription());
            createDepartment(departmentDto);

        });
    }
}
