package com.AstronSpringHomework.App.dto.userDto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private Long id;
    private String name;
    private String email;

    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private Integer age;

}
