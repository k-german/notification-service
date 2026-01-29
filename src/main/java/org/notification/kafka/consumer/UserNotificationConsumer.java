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
//            topics = "user.notifications",
            topics = "${kafka.topic.user-notifications}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String message) throws Exception {
        try {
            UserNotificationEvent event =
                    objectMapper.readValue(message, UserNotificationEvent.class);

            mailService.sendNotification(event.getEmail(), event.getOperation());
        } catch (MailException e) {
            // no mail in smtp log
        } catch (Exception e) {
            // error log
            throw e;
        }
    }
}