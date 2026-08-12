package com.shopsphere.exception;

public class UserDeactivateException extends RuntimeException{
    public UserDeactivateException(String message){
        super(message);
    }
}
