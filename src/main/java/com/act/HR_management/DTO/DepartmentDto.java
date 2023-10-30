package com.act.HR_management.DTO;

import com.act.HR_management.Models.Department;
import com.act.HR_management.Models.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private Long departmentId;
    private String departmentName;
    private String description;
//    private List<Long> employeeIds;

    public static DepartmentDto toDTO(Optional<Department> departmentOptional) {
        Department department = departmentOptional.get();

        return fromEntity(department);
    }
    public static DepartmentDto fromEntity(Department department){
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentId(department.getDepartmentId());
        departmentDto.setDepartmentName(department.getDepartmentName());
        departmentDto.setDescription(department.getDescription());

        return departmentDto;
    }

    public Department toEntity(){
        Department department = new Department();
        department.setDepartmentId(this.departmentId);
        department.setDepartmentName(this.departmentName);
        department.setDescription(this.description);
        return department;
    }

        public static List<DepartmentDto> toDtoList(List<Department> departments){
            return departments.stream()
                    .map(DepartmentDto::fromEntity)
                    .collect(Collectors.toList());   }
        public static List<Department> toEntityList(List<DepartmentDto> departmentDtos){
        return departmentDtos.stream()
                .map(DepartmentDto::toEntity)
                .collect(Collectors.toList());
        }
}
