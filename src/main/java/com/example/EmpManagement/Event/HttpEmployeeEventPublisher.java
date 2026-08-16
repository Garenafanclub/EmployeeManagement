package com.example.EmpManagement.Event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
    public void publishNotify(NotificationRequestEvent event) {
        try {
            System.out.println("Calling Email Microservice for: " + event.getData().getEmail());
            System.out.println("========== OUTGOING NOTIFICATION EVENT ==========");
            System.out.println("Event ID: " + event.getEventId());
            System.out.println("Operation ID: " + event.getOperationId());
            System.out.println("Event Type: " + event.getEventType());
            System.out.println("Employee ID: " + event.getData().getEmpId());
            System.out.println("Email: " + event.getData().getEmail());
            System.out.println("Department ID: " + event.getData().getDepId());
            System.out.println("Temporary Password: " + event.getData().getTempPass());
            System.out.println("=================================================");

            ResponseEntity<Void> response = restClient.post()
                    .uri(webhookUrl)
                    .header("Content-Type", "application/json")
                    .body(event)
                    .retrieve() // Executes the call
                    .toBodilessEntity(); // Expects a successful 202 Accepted back

            System.out.println(
                    "Notification Service responded with: "
                            + response.getStatusCode()
            );

        } catch (Exception e) {
            System.err.println("Microservice connection failed: " + e.getMessage());
        }
    }
}
