package com.AstronSpringHomework.App.notificationService;

import com.AstronSpringHomework.App.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;




@Service
public class KafkaProducer {


    private final KafkaTemplate<String, String> kafkaTemplate;


    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) throws JsonProcessingException {
        kafkaTemplate.send("course", message);
    }

}
