package org.notification.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Отправляет уведомление",
            description = "Отправляет уведомление на email с указанием типа опреауции"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомление успешно отправлено")
    })
    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest request) {
        mailService.sendNotification(request.getEmail(), request.getOperation());
        return ResponseEntity.ok().build();
    }
}
