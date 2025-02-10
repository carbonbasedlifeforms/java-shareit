package ru.practicum.shareit.booking.dto;

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
class BookingInDtoTest {
    @Autowired
    JacksonTester<BookingInDto> json;

    private static final LocalDateTime START = LocalDateTime.of(2020, 1, 1, 1, 1, 1, 0);
    private static final LocalDateTime END = START.plusDays(1);

    @Test
    void testBookingInDto() throws IOException {

        BookingInDto bookingInDto = BookingInDto.builder()
                .id(1L)
                .itemId(1L)
                .start(START)
                .end(END)
                .build();
        JsonContent<BookingInDto> jsonContent = json.write(bookingInDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.start").isEqualTo(START.toString());
        assertThat(jsonContent).extractingJsonPathStringValue("$.end").isEqualTo(END.toString());
    }
}