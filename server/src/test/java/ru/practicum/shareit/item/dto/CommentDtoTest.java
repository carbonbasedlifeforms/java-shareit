package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDtoTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Autowired
    JacksonTester<CommentDto> json;

    @Test
    void testCommentDto() throws IOException {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("Blah blah blah")
                .authorName("Thomas Anderson")
                .created(NOW)
                .build();
        JsonContent<CommentDto> jsonContent = json.write(commentDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text").isEqualTo("Blah blah blah");
        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("Thomas Anderson");
    }
}