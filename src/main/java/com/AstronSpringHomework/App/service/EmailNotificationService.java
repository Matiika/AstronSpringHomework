package com.AstronSpringHomework.App.service;

import com.AstronSpringHomework.App.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {


    @Value("${spring.mail.username}")
    private String fromMail;

    private final JavaMailSender javaMailSender;

    public void notifyByMail(UserEvent userEvent) {

        String mailTo = userEvent.getEmail();
        String subject = userEvent.getEvent();
        String body = null;

        if ("Created".equalsIgnoreCase(userEvent.getEvent())) {
            System.out.println(userEvent.getEmail());
            System.out.println("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
            body = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        } else if ("Deleted".equalsIgnoreCase(userEvent.getEvent())) {
            System.out.println(userEvent.getEmail());
            System.out.println("Здравствуйте! Ваш аккаунт был удалён.");
            body = "Здравствуйте! Ваш аккаунт был удалён.";
        }

        send(mailTo, subject, body);
    }

    public void send(String to, String subject, String body) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        mailMessage.setFrom(fromMail);

        javaMailSender.send(mailMessage);

    }

}
