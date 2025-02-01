package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    private final UserService userService;
    private final EntityManager em;

    private final UserDto userDto = UserDto.builder()
            .name("John Smith")
            .email("testUsers@test.org")
            .build();

    @Test
    void testUserService() {
        userService.createUser(userDto);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertEquals(userDto.getEmail(), user.getEmail());

        UserDto savedUser = userService.getUserById(user.getId());

        assertThat(savedUser.getEmail(), equalTo(userDto.getEmail()));

        userDto.setName("Neo");
        userService.updateUser(user.getId(), userDto);

        List<UserDto> users = userService.getAllUsers();

        assertEquals("Neo", users.getLast().getName());
    }
}