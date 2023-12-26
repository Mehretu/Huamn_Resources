package com.act.HR_management.Utility;

import lombok.Getter;

@Getter
public enum DepartmentEnum {
    DEPARTMENT_ENUM_1("Staff","This is Staff"),
    DEPARTMENT_ENUM_2("Management","A group of management workers"),
    DEPARTMENT_ENUM_3("Custodian","This are Custodians");

    private final String departmentName;
    private final String description;

    DepartmentEnum(String departmentName, String description){
        this.departmentName = departmentName;
        this.description = description;
    }

}
