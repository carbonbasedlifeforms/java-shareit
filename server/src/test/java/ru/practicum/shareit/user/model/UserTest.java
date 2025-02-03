package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    private final User user = User.builder()
            .id(1L)
            .name("Test")
            .email("test@test.com")
            .build();

    @Test
    void testUserModel() {
        assertEquals(1L, user.getId());
        assertEquals("Test", user.getName());
        assertEquals("test@test.com", user.getEmail());
    }
}