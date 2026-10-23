package io.github.dougllasfps.imageliteapi.mapper;

import io.github.dougllasfps.imageliteapi.domain.entity.User;
import io.github.dougllasfps.imageliteapi.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User mapToUser(UserDto userDto){
        return User.builder()
                .email(userDto.email())
                .name(userDto.name())
                .password(userDto.password())
                .build();
    }
}
