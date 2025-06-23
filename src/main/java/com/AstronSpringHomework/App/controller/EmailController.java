package com.AstronSpringHomework.App.controller;

import com.AstronSpringHomework.App.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    EmailNotificationService emailNotificationService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @RequestParam String email,
            @RequestParam String eventType) {

        String message = null;

        if (eventType.equalsIgnoreCase("CREATED")) {
            message = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if (eventType.equalsIgnoreCase("DELETED")) {
            message = "Здравствуйте! Ваш аккаунт был удалён.";
        }

        emailNotificationService.send(email, "Уведомление", message);
        return ResponseEntity.ok("Email отправлен");
    }

}
