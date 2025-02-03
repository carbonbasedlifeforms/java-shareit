package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    private final Item item = Item.builder()
            .id(1L)
            .name("Test")
            .available(true)
            .owner(User.builder().id(1L).build())
            .request(ItemRequest.builder().id(1L).build())
            .description("Test description")
            .build();

    @Test
    void testItemModel() {
        assertEquals(1L, item.getId());
        assertEquals("Test", item.getName());
        assertEquals(true, item.getAvailable());
        assertEquals(1L, item.getOwner().getId());
        assertEquals(1L, item.getRequest().getId());
        assertEquals("Test description", item.getDescription());
    }
}