package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    private final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .description("Test description")
            .requester(User.builder().id(1L).build())
            .created(NOW)
            .build();

    @Test
    void testItemModel() {
        assertEquals(1L, itemRequest.getId());
        assertEquals("Test description", itemRequest.getDescription());
        assertEquals(1L, itemRequest.getRequester().getId());
        assertEquals(NOW, itemRequest.getCreated());
    }
}