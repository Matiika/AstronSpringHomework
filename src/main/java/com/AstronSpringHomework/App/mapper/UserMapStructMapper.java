package com.AstronSpringHomework.App.mapper;

import com.AstronSpringHomework.App.dto.userDto.UserCreateDTO;
import com.AstronSpringHomework.App.dto.userDto.UserDTO;
import com.AstronSpringHomework.App.dto.userDto.UserUpdateDTO;
import com.AstronSpringHomework.App.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapStructMapper {
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    User fromCreateDTO(UserCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserUpdateDTO dto, @MappingTarget User user);
}
