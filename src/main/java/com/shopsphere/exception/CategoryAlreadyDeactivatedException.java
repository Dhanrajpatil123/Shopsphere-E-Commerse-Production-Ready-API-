package com.shopsphere.exception;

public class CategoryAlreadyDeactivatedException extends RuntimeException{
    public CategoryAlreadyDeactivatedException(String message) {
        super(message);
    }
}
