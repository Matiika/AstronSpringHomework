package com.AstronSpringHomework.App.dto.userDto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class UserUpdateDTO {
    @Schema(example = "updateTestName")
    private String name;

    @Schema(example = "updateTest@example.com")
    private String email;

    @Min(value = 0, message = "Возраст не может быть отрицательным")
    @Schema(example = "40")
    private Integer age;

}
