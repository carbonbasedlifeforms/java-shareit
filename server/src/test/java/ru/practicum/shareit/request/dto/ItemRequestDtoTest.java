package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private static final LocalDateTime NOW = LocalDateTime.now();

    @Test
    void testItemRequestDto() throws IOException {
        List<ItemDto> items = List.of(ItemDto.builder().id(1L).build(), ItemDto.builder().id(2L).build());
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Need something")
                .requester(1L)
                .created(NOW)
                .items(items)
                .build();
        JsonContent<ItemRequestDto> jsonContent = json.write(itemRequestDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.description").isEqualTo("Need something");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requester").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.items[1].id").isEqualTo(2);
    }
}