package com.AstronSpringHomework.App.controller;

import com.AstronSpringHomework.App.service.EmailNotificationService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    EmailNotificationService emailNotificationService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @Parameter(description = "Email получателя", example = "test@example.com")
            @RequestParam String email,
            @Parameter(description = "Тип события", example = "CREATED")
            @RequestParam String eventType) {

        String message = null;

        if (eventType.equalsIgnoreCase("CREATED")) {
            message = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if (eventType.equalsIgnoreCase("DELETED")) {
            message = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            return ResponseEntity.badRequest()
                    .body("Нет такого варианта события eventType");
        }

        emailNotificationService.send(email, "Уведомление", message);
        return ResponseEntity.ok("Email отправлен");
    }

}
