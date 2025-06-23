package com.AstronSpringHomework.App.controller;

import com.AstronSpringHomework.App.configuration.TestMailConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestMailConfig.class)
public class EmailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void sendEmail_shouldSendCorrectMessage() throws Exception {
        // Выполняем POST-запрос к /send-email
        mockMvc.perform(post("/api/send-email")
                        .param("email", "test@example.com")
                        .param("eventType", "CREATED"))
                .andExpect(status().isOk())
                .andExpect(content().string("Email отправлен"));

        // Перехватываем отправленное письмо
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        // Проверяем содержимое письма
        SimpleMailMessage message = captor.getValue();
        assertEquals("test@example.com", message.getTo()[0]);
        assertEquals("Уведомление", message.getSubject());
        assertTrue(message.getText().contains("успешно создан"));
    }
}

