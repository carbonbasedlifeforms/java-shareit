package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    private final Comment comment = Comment.builder()
            .id(1L)
            .text("Test")
            .author(User.builder().id(1L).build())
            .item(Item.builder().id(1L).build())
            .created(NOW)
            .build();

    @Test
    void testCommentModel() {
        assertEquals(1L, comment.getId());
        assertEquals("Test", comment.getText());
        assertEquals(1L, comment.getAuthor().getId());
        assertEquals(1L, comment.getItem().getId());
        assertEquals(NOW, comment.getCreated());
    }
}