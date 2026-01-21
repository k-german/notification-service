package org.notification.web.controller;

import org.notification.mail.service.MailService;
import org.notification.web.dto.NotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final MailService mailService;

    public NotificationController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        mailService.sendNotification(request.getEmail(), request.getOperation());
        return ResponseEntity.ok().build();
    }
}
