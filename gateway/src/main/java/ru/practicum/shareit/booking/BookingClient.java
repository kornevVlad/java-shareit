package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(BookItemRequestDto bookingDto, Long bookerId) {
        return post("", bookerId, bookingDto);
    }

    public ResponseEntity<Object> updateBooking(Long bookingId, Long userId, Boolean approved) {
        return patch("/" + bookingId + "?approved=" + approved, userId, null, null);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/"+ bookingId, userId);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        String path = "?state=" + state.name() + "&from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path, userId, null);
    }

    public ResponseEntity<Object> getBookingsByBookerItems(long ownerId, BookingState state,
                                                           Integer from, Integer size) {
        String path = "/owner?state=" + state.name() + "&from=" + from;
        if (size != null) {
            path += "&size=" + size;
        }
        return get(path, ownerId, null);
    }
}