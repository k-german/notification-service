package org.notification.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class UserNotificationEvent {
    OperationType operation;
    String email;
}
