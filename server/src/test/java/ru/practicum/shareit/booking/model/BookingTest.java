package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    private static final LocalDateTime START = LocalDateTime.of(2020, 1, 1, 1, 1, 1);
    private static final LocalDateTime END = LocalDateTime.of(2020, 1, 2, 1, 1, 1);

    private final Booking booking = Booking.builder()
            .id(1L)
            .item(Item.builder().id(1L).build())
            .booker(User.builder().id(1L).build())
            .status(BookingStatus.WAITING)
            .start(START)
            .end(END)
            .build();

    @Test
    void testBookingModel() {
        assertEquals(1L, booking.getId());
        assertEquals(1L, booking.getItem().getId());
        assertEquals(1L, booking.getBooker().getId());
        assertEquals(BookingStatus.WAITING, booking.getStatus());
        assertEquals(START, booking.getStart());
        assertEquals(END, booking.getEnd());
    }
}