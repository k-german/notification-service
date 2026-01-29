package org.notification.kafka.consumer;

import org.notification.kafka.dto.UserNotificationEvent;
import org.notification.mail.service.MailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserNotificationConsumer {

    private final MailService mailService;

    public UserNotificationConsumer(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(
//            topics = "user.notifications",
            topics = "${kafka.topic.user-notifications}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(UserNotificationEvent event) {
        mailService.sendNotification(event.getEmail(), event.getOperation());
    }
}