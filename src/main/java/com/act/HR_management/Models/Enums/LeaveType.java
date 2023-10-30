package com.act.HR_management.Models.Enums;

public enum LeaveType {
    ANNUAL_LEAVE("Annual Leave"),
    SICK_LEAVE("Sick Leave"),
    MATERNITY_LEAVE("Maternity Leave"),
    PATERNITY_LEAVE("Paternity Leave");

    private final String statusName;

    LeaveType(String statusName){
        this.statusName = statusName;
    }
    public String getStatusName(){
        return statusName;
    }
}
