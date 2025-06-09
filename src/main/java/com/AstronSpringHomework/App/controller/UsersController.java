package com.AstronSpringHomework.App.controller;

import com.AstronSpringHomework.App.dto.userDto.UserCreateDTO;
import com.AstronSpringHomework.App.dto.userDto.UserDTO;
import com.AstronSpringHomework.App.dto.userDto.UserUpdateDTO;
import com.AstronSpringHomework.App.exeption.EmailAlreadyExistsException;
import com.AstronSpringHomework.App.exeption.ResourceNotFoundException;
import com.AstronSpringHomework.App.mapper.UserMapStructMapper;
import com.AstronSpringHomework.App.model.User;
import com.AstronSpringHomework.App.repository.UserRepository;
import com.AstronSpringHomework.App.validator.UserCreateDTOValidator;
import com.AstronSpringHomework.App.validator.UserUpdateDTOValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapStructMapper userMapStructMapper;

    @Autowired
    UserCreateDTOValidator userCreateDTOValidator;

    @Autowired
    UserUpdateDTOValidator userUpdateDTOValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if ("userCreateDTO".equals(binder.getObjectName())) {
            binder.addValidators(userCreateDTOValidator);
        }
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        User user = userMapStructMapper.fromCreateDTO(userCreateDTO);
        userRepository.save(user);
        return userMapStructMapper.toDTO(user);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        List<UserDTO> userDTOList = userRepository.findAll()
                .stream()
                .map(user -> userMapStructMapper.toDTO(user))
                .toList();
        return userDTOList;
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
        return userMapStructMapper.toDTO(user);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@PathVariable Long id,
                          @RequestBody UserUpdateDTO userUpdateDTO) {
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

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
        userRepository.deleteById(id);
    }



}
