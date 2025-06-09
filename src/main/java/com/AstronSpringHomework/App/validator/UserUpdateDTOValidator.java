package com.AstronSpringHomework.App.validator;

import com.AstronSpringHomework.App.repository.UserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.AstronSpringHomework.App.dto.userDto.UserUpdateDTO;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserUpdateDTOValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserUpdateDTO.class.isAssignableFrom(clazz);
    }

    @Autowired
    UserRepository userRepository;

    @Override
    public void validate(Object target, Errors errors) {

    }

    public void validateUpdateDTO(UserUpdateDTO dto) {
        if (dto.getName() != null && dto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Имя не может быть пустым");
        }

        if (dto.getEmail() != null && !EmailValidator.getInstance().isValid(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Невалидный email");
        }

        if (dto.getAge() != null && dto.getAge() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Возраст не может быть отрицательным");
        }
    }
}
