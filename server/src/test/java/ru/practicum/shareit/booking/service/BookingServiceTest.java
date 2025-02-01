package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {
    private static final LocalDateTime START = LocalDateTime.of(2020, 1, 1, 1, 1, 1, 0);
    private static final LocalDateTime END = START.plusDays(1);

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("New user")
            .email("testBookings@test.org")
            .build();

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("New item")
            .available(true)
            .description("My very new item")
            .build();
    private final BookingInDto bookingInDto = BookingInDto.builder()
            .itemId(1L)
            .start(START)
            .end(END)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .start(START)
            .end(END)
            .item(itemDto)
            .booker(userDto)
            .build();

    @Test
    void testBookingService() {
        UserDto savedUser = userService.createUser(userDto);
        itemService.createItem(savedUser.getId(), itemDto);
        BookingDto savedBooking = bookingService.createBooking(savedUser.getId(), bookingInDto);

        assertThat(savedBooking.getStatus(), equalTo(BookingStatus.WAITING));

        bookingService.changeBookingApproved(savedUser.getId(), savedBooking.getId(), "true");

        BookingDto updatedBooking = bookingService.getBooking(savedUser.getId(), savedBooking.getId());
        assertThat(updatedBooking.getStatus(), equalTo(BookingStatus.APPROVED));

        List<BookingDto> userBookings = bookingService.getUserBookings(savedUser.getId(), "ALL");

        assertThat(userBookings.size(), equalTo(1));

        List<BookingDto> ownerItemsBookings = bookingService.getBookingsForOwnerItems(savedUser.getId(), "ALL");

        assertThat(ownerItemsBookings.size(), equalTo(1));
    }
}