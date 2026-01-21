package org.notification.mail.service;

import org.notification.kafka.dto.OperationType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${notification.mail.from}")
    private String from;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendNotification(String email, OperationType operation) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Test message");

        if (operation == OperationType.CREATE) {
            message.setText("Hello! Your account has been SUCCESSFULLY CREATED.");
        } else if (operation == OperationType.DELETE) {
            message.setText("Hello! Your account has been DELETED");
        }

        mailSender.send(message);
    }
}
