package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.common.GlobalVariables.USER_ID_HEADER;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                    @Valid @RequestBody BookingInDto bookingInDto) {
        log.info("create Booking: {}", bookingInDto);
        return bookingService.createBooking(userId, bookingInDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeBookingApproved(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam(name = "approved") String approved) {
        log.info("change booking approved status: {}", approved);
        return bookingService.changeBookingApproved(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader(name = USER_ID_HEADER) Long userId, @PathVariable Long bookingId) {
        log.info("get booking by id: {}", bookingId);
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                            @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("get all bookings for current user with id: {}", userId);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwnerItems(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                     @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("get all booking for owner's items, userId: {}", userId);
        return bookingService.getBookingsForOwnerItems(userId, state);
    }
}
