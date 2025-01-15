package ru.practicum.shareit.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final static LocalDateTime NOW = LocalDateTime.now();
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto createBooking(Long userId, BookingInDto bookingInDto) {
        User user = checkUserExists(userId);
        Item item = itemRepository.findById(bookingInDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));
        if (!item.getAvailable())
            throw new ValidationException("Item is not available");
        if (bookingInDto.getStart().isEqual(bookingInDto.getEnd()))
            throw new ValidationException("Start and end dates are equal");
        Booking booking = BookingMapper.toBooking(bookingInDto, user, item);
        booking.setStatus(BookingStatus.WAITING);
        log.info("Booking created: {}", booking);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto changeBookingApproved(Long userId, Long bookingId, String approved) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("User is not valid"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking with id: %d not found".formatted(bookingId)));
        if (approved.equals("true"))
            booking.setStatus(BookingStatus.APPROVED);
        else
            booking.setStatus(BookingStatus.REJECTED);
        log.info("Booking approved status updated: {}", booking);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.getByUserIdOrOwnerItem(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Booking with id: %d and user id: %d not found"
                        .formatted(bookingId, userId)));
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String state) {
        List<Booking> bookings;
        bookings = switch (BookingState.getBookingState(state)) {
            case ALL -> bookingRepository.findAllByBooker_id(userId);
            case PAST -> bookingRepository.findAllByBooker_idAndEndBefore(userId, NOW);
            case FUTURE -> bookingRepository.findAllByBooker_idAndStartAfter(userId, NOW);
            case CURRENT -> bookingRepository.findAllByBookerAndNowBetweenStartAndEnd(userId, NOW);
            case WAITING -> bookingRepository.findAllByBooker_idAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByBooker_idAndStatus(userId, BookingStatus.REJECTED);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsForOwnerItems(Long userId, String state) {
        checkUserExists(userId);
        List<Booking> bookings;
        bookings = switch (BookingState.getBookingState(state)) {
            case ALL -> bookingRepository.findAllByItem_Owner_id(userId);
            case PAST -> bookingRepository.findAllByItem_Owner_idAndEndBefore(userId, NOW);
            case FUTURE -> bookingRepository.findAllByItem_Owner_idAndStartAfter(userId, NOW);
            case CURRENT -> bookingRepository.findAllByOwnerAndNowBetweenStartAndEnd(userId, NOW);
            case WAITING -> bookingRepository.findAllByItem_Owner_idAndStatus(userId, BookingStatus.WAITING);
            case REJECTED -> bookingRepository.findAllByItem_Owner_idAndStatus(userId, BookingStatus.REJECTED);
        };
        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    private User checkUserExists(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
