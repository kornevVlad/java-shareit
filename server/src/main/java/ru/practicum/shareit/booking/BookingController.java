package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping() //создание бронирования
    public BookingDto createBooking(@Valid @RequestBody BookingRequestDto bookingDto,
                                    @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("Создание предмета бронирования ID={}, пользователь ID={}", bookingDto.getItemId(), bookerId);
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId,
                             @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam Boolean approved) {
        log.info("Обновление статуса бронирования с ID={}, Пользователь предмета ID={}, Статус {}",
                bookingId, userId, approved);
        return bookingService.updateBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(defaultValue = "0") Integer from,
                                                  @RequestParam(required = false) Integer size) {
        return bookingService.getBookingsByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByBookerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(required = false) Integer size) {
        return bookingService.getBookingsByBookerItems(ownerId, state, from, size);
    }
}