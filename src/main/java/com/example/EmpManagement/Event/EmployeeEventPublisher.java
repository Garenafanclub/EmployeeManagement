package com.example.EmpManagement.Event;

public interface EmployeeEventPublisher {
    void publish(EmployeeCreatedEvent event);
}
