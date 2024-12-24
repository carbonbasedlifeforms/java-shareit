package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getAll() {
        log.info("Getting all users");
        return users.values().stream().toList();
    }

    @Override
    public User getById(Long id) {
        log.info("Getting user by ID {}", id);
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("User ID %d not found".formatted(id)));
    }

    @Override
    public Boolean isDuplicatedEmailExists(String email, Long id) {
        log.info("Checking duplicated email {}", email);
        return users.values().stream()
                .filter(user -> !user.getId().equals(id))
                .anyMatch(otherUser -> otherUser.getEmail().equals(email));
    }

    @Override
    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Creating user {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.info("Updating user {}", user);
        return user;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user with ID {}", id);
        users.remove(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
