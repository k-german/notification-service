package org.notification.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.notification.kafka.dto.UserNotificationEvent;
import org.notification.mail.service.MailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Component;

@Component
public class UserNotificationConsumer {

    private final MailService mailService;
    private final ObjectMapper objectMapper;

    public UserNotificationConsumer(MailService mailService, ObjectMapper objectMapper) {
        this.mailService = mailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${kafka.topic.user-notifications}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String message) throws Exception {

        System.out.println("Received Kafka message: " + message);

        try {
            UserNotificationEvent event =
                    objectMapper.readValue(message, UserNotificationEvent.class);

            mailService.sendNotification(event.getEmail(), event.getOperation());
        } catch (MailException e) {
            System.out.println("Mail sending failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\"sendNotification\" failed: " + e.getMessage());
            throw e;
        }
    }
}