package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private static final LocalDateTime START = LocalDateTime.of(2020, 1, 1, 1, 1, 1, 0);
    private static final LocalDateTime END = START.plusDays(1);
    @Autowired
    JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws IOException {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(START)
                .end(END)
                .item(ItemDto.builder().id(1L).build())
                .booker(UserDto.builder().id(1L).build())
                .status(BookingStatus.WAITING)
                .build();
        JsonContent<BookingDto> jsonContent = json.write(bookingDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo(START.toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo(END.toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
    }

}