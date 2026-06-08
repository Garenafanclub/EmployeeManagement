package com.example.EmpManagement.Exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException{

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Resource not found: %s with %s = %s", resourceName, fieldName, fieldValue),
                HttpStatus.NOT_FOUND);
    }
}
