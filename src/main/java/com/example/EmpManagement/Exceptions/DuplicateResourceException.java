package com.example.EmpManagement.Exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException{

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Duplicate resource: %s with %s = %s already exists", resourceName, fieldName, fieldValue),
                HttpStatus.CONFLICT);
    }
}
