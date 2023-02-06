package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class
BookingController {
	private final BookingClient bookingClient;

	@PostMapping() //создание бронирования
	public ResponseEntity<Object> createBooking(@Valid @RequestBody BookItemRequestDto bookingDto,
									@RequestHeader("X-Sharer-User-Id") Long bookerId) {
		log.info("Создание предмета бронирования ID={}, пользователь ID={}", bookingDto.getItemId(), bookerId);
		return bookingClient.createBooking(bookingDto, bookerId);
	}

	@PatchMapping("/{bookingId}") //обновление бронирования
	public ResponseEntity<Object> update(@PathVariable Long bookingId,
							 @RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam Boolean approved) {
		log.info("Обновление статуса бронирования с ID={}, Пользователь предмета ID={}, Статус {}",
				bookingId, userId, approved);
		return bookingClient.updateBooking(bookingId, userId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}


	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(required = false) Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingsByBookerItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
			@Positive @RequestParam(required = false) Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, ownerId={}, from={}, size={}", stateParam, ownerId, from, size);
		return bookingClient.getBookingsByBookerItems(ownerId, state, from, size);
	}
}