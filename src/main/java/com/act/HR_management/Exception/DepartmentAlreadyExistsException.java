package com.act.HR_management.Exception;

public class DepartmentAlreadyExistsException extends RuntimeException{
    public DepartmentAlreadyExistsException(String message){
        super(message);
    }
}

