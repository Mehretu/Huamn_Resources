package com.act.HR_management.Models.Enums;

public enum AttendanceStatus {
    PRESENT("Present"),
    LATE("Late"),
    ABSENT("Absent");

    private final String statusName;
    AttendanceStatus(String statusName){
        this.statusName = statusName;
    }
    public String getStatusName(){
        return statusName;
    }
}
