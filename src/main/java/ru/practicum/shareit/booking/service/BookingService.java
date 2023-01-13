package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingRequestDto bookingDto, Long bookerId);

    BookingDto updateBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getBookingsByBookerId(Long bookerId, String state);

    List<BookingDto> getBookingsByBookerItems(Long ownerId, String state);
}
