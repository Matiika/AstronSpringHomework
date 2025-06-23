package com.AstronSpringHomework.App.service;

import com.AstronSpringHomework.App.dto.UserEvent;
import com.AstronSpringHomework.App.dto.userDto.UserCreateDTO;
import com.AstronSpringHomework.App.dto.userDto.UserDTO;
import com.AstronSpringHomework.App.dto.userDto.UserUpdateDTO;
import com.AstronSpringHomework.App.exeption.EmailAlreadyExistsException;
import com.AstronSpringHomework.App.exeption.ResourceNotFoundException;
import com.AstronSpringHomework.App.mapper.UserMapStructMapper;
import com.AstronSpringHomework.App.model.User;
import com.AstronSpringHomework.App.notificationService.KafkaProducer;
import com.AstronSpringHomework.App.repository.UserRepository;
import com.AstronSpringHomework.App.validator.UserCreateDTOValidator;
import com.AstronSpringHomework.App.validator.UserUpdateDTOValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapStructMapper userMapStructMapper;

    @Autowired
    UserCreateDTOValidator userCreateDTOValidator;

    @Autowired
    UserUpdateDTOValidator userUpdateDTOValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if ("userCreateDTO".equals(binder.getObjectName())) {
            binder.addValidators(userCreateDTOValidator);
        }
    }

    public UserDTO create(UserCreateDTO userCreateDTO) throws JsonProcessingException {
        User user = userMapStructMapper.fromCreateDTO(userCreateDTO);
        userRepository.save(user);

        UserEvent userEvent = new UserEvent(user.getEmail(), "Created");
        String userEventJson = objectMapper.writeValueAsString(userEvent);
        kafkaProducer.sendMessage(userEventJson);

        return userMapStructMapper.toDTO(user);
    }


    public List<UserDTO> index() {
        List<UserDTO> userDTOList = userRepository.findAll()
                .stream()
                .map(user -> userMapStructMapper.toDTO(user))
                .toList();
        return userDTOList;
    }


    public UserDTO show(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
        return userMapStructMapper.toDTO(user);
    }


    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.getEmail() != null &&
                userRepository.existsByEmailAndIdNot(userUpdateDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("Пользователь с таким Email уже существует");
        }
        userUpdateDTOValidator.validateUpdateDTO(userUpdateDTO);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
        userUpdateDTO.setId(id);
        userMapStructMapper.updateEntityFromDTO(userUpdateDTO, user);
        userRepository.save(user);
        return userMapStructMapper.toDTO(user);
    }


    public void delete(Long id) throws JsonProcessingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
        userRepository.deleteById(id);

        UserEvent userEvent = new UserEvent(user.getEmail(), "Deleted");
        String userEventJson = objectMapper.writeValueAsString(userEvent);
        kafkaProducer.sendMessage(userEventJson);
    }

}
