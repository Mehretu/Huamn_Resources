package com.act.HR_management.Models.Enums;

public enum LeaveStatus {
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    CANCELLED("Canceled"),
    CREATED("Created");

    private final String statusName;

    LeaveStatus(String statusName){
        this.statusName = statusName;
    }
    public String getStatusName(){
        return statusName;
    }


}
