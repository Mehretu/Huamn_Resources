package com.act.HR_management.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private String phoneNumber;

    private String email;

    private LocalDate hireDate;

    private double baseSalary;

    private String jobPosition;

    private double benefits;

    private String employeeId;

    @OneToMany
    @JsonIgnore
    private List<Overtime> overtime;

    @OneToMany
    @JsonIgnore
    private List<Bonus> bonuse;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @JsonIgnore
    @OneToMany()
    private List<Leave> leaves;



    @PrePersist
    public void prePersist(){
        hireDate = LocalDate.now();
    }



}
