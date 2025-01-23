package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("get all users");
        // TODO pagination
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("get user by id {}", id);
        return UserMapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User ID %d not found".formatted(id))));

    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        log.info("create user {}", userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User userForUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User ID %d not found".formatted(id)));
        UserMapper.updateUserFields(userDto, userForUpdate);
        log.info("update user with id {}", id);
        return UserMapper.toUserDto(userForUpdate);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        log.info("delete user with id {}", id);
        userRepository.deleteById(id);
    }
}
