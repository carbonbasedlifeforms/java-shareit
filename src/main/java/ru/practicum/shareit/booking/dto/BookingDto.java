package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.enums.BookingStatus;

import java.time.LocalDateTime;

public class BookingDto {
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long item;

    private Long booker;

    private BookingStatus status;
}
