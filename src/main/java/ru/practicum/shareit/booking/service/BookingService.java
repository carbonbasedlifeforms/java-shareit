package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId ,BookingInDto bookingInDto);

    BookingDto changeBookingApproved(Long userId, Long bookingId, String approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getUserBookings(Long userId, String state);

    List<BookingDto> getBookingsForOwnerItems(Long userId, String state);
}
