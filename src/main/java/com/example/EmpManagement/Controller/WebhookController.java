package com.example.EmpManagement.Controller;

import com.example.EmpManagement.Event.NotificationCompletedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    @PostMapping("/notification-completed")
    public ResponseEntity<Void> handleNotificationCompleted(@RequestBody NotificationCompletedEvent event)
    {
        System.out.println("==========================================");
        System.out.println("NOTIFICATION COMPLETION RECEIVED");
        System.out.println("Event ID: " + event.getEventId());
        System.out.println("Operation ID: " + event.getOperationId());
        System.out.println("Event Type: " + event.getEventType());
        System.out.println("Employee ID: "
                + event.getData().getEmployeeId());
        System.out.println("Status: "
                + event.getData().getStatus());
        System.out.println("==========================================");

        return ResponseEntity.ok().build();
    }
}
