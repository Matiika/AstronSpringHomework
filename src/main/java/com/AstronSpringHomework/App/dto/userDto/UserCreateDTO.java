package com.AstronSpringHomework.App.dto.userDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {
    @NotBlank(message = "Имя обязательно")
    @Schema(example = "testName")
    private String name;

    @Email(message = "Некорректный email")
    @NotBlank(message = "Email обязателен")
    @Schema(example = "test@example.com")
    private String email;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Schema(example = "30")
    private Integer age;
}

