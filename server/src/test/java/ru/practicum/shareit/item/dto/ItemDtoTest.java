package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testItemDto() throws IOException {
        List<CommentDto> comments = List.of(CommentDto.builder().id(1L).build(), CommentDto.builder().id(2L).build());
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("New item")
                .available(true)
                .description("My very new item")
                .requestId(1L)
                .comments(comments)
                .build();
        JsonContent<ItemDto> jsonContent = json.write(itemDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("New item");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("My very new item");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.comments[1].id").isEqualTo(2);
    }
}