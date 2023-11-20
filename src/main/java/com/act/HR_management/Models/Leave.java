package com.act.HR_management.Models;

import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.Enums.LeaveType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;


    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    private String notes;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @PrePersist
    protected void onCreate(){
        createdOn = LocalDateTime.now();
        updatedOn = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate(){
        updatedOn = LocalDateTime.now();
    }

}
