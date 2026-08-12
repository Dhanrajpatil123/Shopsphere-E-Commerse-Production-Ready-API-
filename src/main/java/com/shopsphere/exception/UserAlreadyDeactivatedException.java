package com.shopsphere.exception;

public class UserAlreadyDeactivatedException extends RuntimeException{
    public UserAlreadyDeactivatedException(String message){
        super(message);
    }
}
