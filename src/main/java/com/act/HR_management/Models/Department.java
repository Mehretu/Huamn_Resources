package com.act.HR_management.Models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departmentName;

    private String description;

    private LocalDate creationDate;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Employee> employee;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<Attendance> attendanceList;
}
