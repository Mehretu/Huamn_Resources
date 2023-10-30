package com.act.HR_management.Exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String message){
        super(message);
    }
}
