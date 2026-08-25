package com.example.EmpManagement.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestEvent {

    private UUID eventId;
    private UUID operationId;
    private String eventType;
    private Instant occurredAt;
    private NotificationData data;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class NotificationData{
        private Long empId;
        private String email;
        private Long depId;
        private String tempPass;
    }
}
