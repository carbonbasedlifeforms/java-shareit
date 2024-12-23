package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static void updateUserFields(UserDto userDto, User user) {
        if (userDto.hasName()) {
            user.setName(userDto.getName());
        }
        if (userDto.hasEmail()) {
            user.setEmail(userDto.getEmail());
        }
    }
}