package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

// пока сделал без использования MapStruct, оставлю себе как TODO
@UtilityClass
public class BookingMapper {
    public Booking toBooking(BookingInDto bookingInDto, User booker, Item item) {
        return Booking.builder()
                .id(bookingInDto.getId())
                .start(bookingInDto.getStart())
                .end(bookingInDto.getEnd())
                .booker(booker)
                .item(item)
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }
}
