package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookingInDto {
    private Long id;

    private Long itemId;

    private LocalDateTime start;

    private LocalDateTime end;
}
