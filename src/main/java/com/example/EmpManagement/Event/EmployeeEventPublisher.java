package com.example.EmpManagement.Event;

public interface EmployeeEventPublisher {
    void publishNotify(NotificationRequestEvent event);
}
