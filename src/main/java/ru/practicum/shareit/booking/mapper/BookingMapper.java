package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
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
                .booker(UserDto.builder()
                        .id(booking.getBooker().getId())
                        .name(booking.getBooker().getName())
                        .email(booking.getBooker().getEmail())
                        .build())
                .item(ItemDto.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .description(booking.getItem().getDescription())
                        .available(booking.getItem().getAvailable())
                        .build())
                .status(booking.getStatus())
                .build();
    }
}
