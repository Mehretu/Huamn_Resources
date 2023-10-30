package com.act.HR_management.Models;

import com.act.HR_management.Models.Enums.LeaveStatus;
import com.act.HR_management.Models.Enums.LeaveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;


    private double duration;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus;

    private String notes;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

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
