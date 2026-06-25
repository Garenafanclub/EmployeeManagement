package com.example.EmpManagement.Service.Imp;

import com.example.EmpManagement.Model.Employee;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class OnboardingNotificationService {

    private final RestClient restClient;

    public OnboardingNotificationService(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendWelcomeEmail(Employee employee, String tempPassword)
    {
        String targetUrl = "http://localhost:8083/api/v1/notifications/welcome";

        Map<String, String> payload = new HashMap<>();
        payload.put("event", "NEW_HIRE_ONBOARDED");
        payload.put("message", "Your temporary password is: " + tempPassword);
        payload.put("email", employee.getEmail());
        payload.put("departmentId", String.valueOf(employee.getDepartment().getId()));

        try {
            System.out.println("Calling Email Microservice for: " + employee.getEmail());

            restClient.post()
                    .uri(targetUrl)
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .retrieve() // Executes the call
                    .toBodilessEntity(); // Expects a successful 200 OK back

            System.out.println("Successfully handed off to Email Microservice!");

        } catch (Exception e) {
            System.err.println("Microservice connection failed: " + e.getMessage());
        }
    }
}
