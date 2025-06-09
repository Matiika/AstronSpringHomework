package com.AstronSpringHomework.App.validator;

import com.AstronSpringHomework.App.dto.userDto.UserCreateDTO;
import com.AstronSpringHomework.App.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserCreateDTOValidator implements Validator {

    @Autowired
    private final UserRepository userRepository;

    public UserCreateDTOValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserCreateDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateDTO dto = (UserCreateDTO) target;

        if (userRepository.existsByEmail(dto.getEmail())) {
            errors.rejectValue("email", "duplicate", "Этот email уже используется");
        }
    }
}
