package ru.practicum.shareit.booking.enums;

import ru.practicum.shareit.exception.ValidationException;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED;

    public static BookingState getBookingState(String state) {
        try {
            return BookingState.valueOf(state);
        } catch (RuntimeException e) {
            throw new ValidationException("Wrong state %s".formatted(state));
        }
    }
}
