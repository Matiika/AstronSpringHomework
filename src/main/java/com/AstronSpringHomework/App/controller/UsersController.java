package com.AstronSpringHomework.App.controller;

import com.AstronSpringHomework.App.dto.userDto.UserCreateDTO;
import com.AstronSpringHomework.App.dto.userDto.UserDTO;
import com.AstronSpringHomework.App.dto.userDto.UserUpdateDTO;

import com.AstronSpringHomework.App.service.UserService;
import com.AstronSpringHomework.App.validator.UserCreateDTOValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import java.util.List;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    UserCreateDTOValidator userCreateDTOValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        if ("userCreateDTO".equals(binder.getObjectName())) {
            binder.addValidators(userCreateDTOValidator);
        }
    }

    @Autowired
    UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserDTO> create(@Valid @RequestBody UserCreateDTO userCreateDTO) throws JsonProcessingException {
        UserDTO userDTO = userService.create(userCreateDTO);
        Long id = userDTO.getId();
        return EntityModel.of(userDTO,
                linkTo(methodOn(UsersController.class).show(id)).withSelfRel(),
                linkTo(methodOn(UsersController.class).index()).withRel("all-users"),
                linkTo(methodOn(UsersController.class).delete(id)).withRel("delete")
        );
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        return userService.index();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDTO> show(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) throws JsonProcessingException {
        UserDTO userDTO = userService.show(id);

        return EntityModel.of(userDTO,
                linkTo(methodOn(UsersController.class).show(id)).withSelfRel(),
                linkTo(methodOn(UsersController.class).index()).withRel("all-users"),
                linkTo(methodOn(UsersController.class).delete(id)).withRel("delete")
        );
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<UserDTO> update(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id,
                          @RequestBody UserUpdateDTO userUpdateDTO) throws JsonProcessingException {
        UserDTO userDTO = userService.update(id, userUpdateDTO);

        return EntityModel.of(userDTO,
                linkTo(methodOn(UsersController.class).show(id)).withSelfRel(),
                linkTo(methodOn(UsersController.class).index()).withRel("all-users"),
                linkTo(methodOn(UsersController.class).delete(id)).withRel("delete")
        );

    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long id) throws JsonProcessingException {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/users")
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserDTO create(@Valid @RequestBody UserCreateDTO userCreateDTO) {
//        User user = userMapStructMapper.fromCreateDTO(userCreateDTO);
//        userRepository.save(user);
//        return userMapStructMapper.toDTO(user);
//    }
//
//    @GetMapping("/users")
//    @ResponseStatus(HttpStatus.OK)
//    public List<UserDTO> index() {
//        List<UserDTO> userDTOList = userRepository.findAll()
//                .stream()
//                .map(user -> userMapStructMapper.toDTO(user))
//                .toList();
//        return userDTOList;
//    }
//
//    @GetMapping("/users/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public UserDTO show(@PathVariable Long id) {
//        var user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
//        return userMapStructMapper.toDTO(user);
//    }
//
//    @PutMapping("/users/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public UserDTO update(@PathVariable Long id,
//                          @RequestBody UserUpdateDTO userUpdateDTO) {
//        if (userUpdateDTO.getEmail() != null &&
//                userRepository.existsByEmailAndIdNot(userUpdateDTO.getEmail(), id)) {
//            throw new EmailAlreadyExistsException("Пользователь с таким Email уже существует");
//        }
//        userUpdateDTOValidator.validateUpdateDTO(userUpdateDTO);
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
//        userUpdateDTO.setId(id);
//        userMapStructMapper.updateEntityFromDTO(userUpdateDTO, user);
//        userRepository.save(user);
//        return userMapStructMapper.toDTO(user);
//    }
//
//    @DeleteMapping("/users/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void delete(@PathVariable Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id=" + id + " не найден"));
//        userRepository.deleteById(id);
//    }



}
