package com.example.EmpManagement.Event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HttpEmployeeEventPublisher implements EmployeeEventPublisher{

    private final RestClient restClient;

    @Value("${notification.webhook.url}")
    private String webhookUrl;

    public HttpEmployeeEventPublisher(RestClient restClientbuilder) {
        this.restClient = restClientbuilder;
    }

    @Override
    public void publish(EmployeeCreatedEvent event) {
        try {
            System.out.println("Calling Email Microservice for: " + event.getData().getEmail());

            restClient.post()
                    .uri(webhookUrl)
                    .header("Content-Type", "application/json")
                    .body(event)
                    .retrieve() // Executes the call
                    .toBodilessEntity(); // Expects a successful 200 OK back

            System.out.println("Successfully handed off to Email Microservice!");

        } catch (Exception e) {
            System.err.println("Microservice connection failed: " + e.getMessage());
        }
    }
}
