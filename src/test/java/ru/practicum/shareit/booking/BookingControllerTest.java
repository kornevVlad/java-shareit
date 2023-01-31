package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.ItemBookingDto;
import ru.practicum.shareit.booking.dto.UserBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    void createBooking() throws Exception {
        when(bookingService.createBooking(any(), any(Long.class)))
                .thenReturn(getBookingDto());

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(getBookingRequestDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getBookingDto().getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(getBookingDto().getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(getBookingDto().getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(getBookingDto().getStatus().toString())));
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingService.updateBooking(any(Long.class), any(Long.class), any(Boolean.class)))
                .thenReturn(getBookingDto());
        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(getBookingDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .queryParam("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getBookingDto().getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(getBookingDto().getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(getBookingDto().getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(getBookingDto().getStatus().toString())));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(any(Long.class), any(Long.class)))
                .thenReturn(getBookingDto());

        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(getBookingDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(getBookingDto().getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(getBookingDto().getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end",
                        is(getBookingDto().getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(getBookingDto().getStatus().toString()), BookingStatus.class));
    }

    @Test
    void getBookingsByBookerId() throws Exception {
        List<BookingDto> bookingDtos = new ArrayList<>();
        bookingDtos.add(getBookingDto());
        when(bookingService.getBookingsByBookerId(any(Long.class), any(String.class),
                any(Integer.class), nullable(Integer.class)))
                .thenReturn(bookingDtos);

        mvc.perform(get("/bookings")

                        .content(mapper.writeValueAsString(bookingDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(getBookingDto().getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(getBookingDto().getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(getBookingDto().getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].item.id", is(getBookingDto().getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(getBookingDto().getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(getBookingDto().getStatus().toString())));
    }

    @Test
    void getBookingsByBookerItems() throws Exception {
        List<BookingDto> bookingDtos = new ArrayList<>();
        bookingDtos.add(getBookingDto());
        when(bookingService.getBookingsByBookerItems(any(Long.class),any(String.class),
                any(Integer.class), nullable(Integer.class)))
                .thenReturn(bookingDtos);

        mvc.perform(get("/bookings/owner?from=0&size=2")
                        .content(mapper.writeValueAsString(bookingDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(getBookingDto().getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(getBookingDto().getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(getBookingDto().getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].item.id", is(getBookingDto().getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(getBookingDto().getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].status", is(getBookingDto().getStatus().toString())));
    }

    private BookingRequestDto getBookingRequestDto() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.of(2023,5,5,1,10));
        bookingRequestDto.setEnd(LocalDateTime.of(2023,5,10,1,10));
        return bookingRequestDto;
    }

    private BookingDto getBookingDto() {
        ItemBookingDto itemBookingDto = new ItemBookingDto();
        itemBookingDto.setId(1L);
        itemBookingDto.setName("Предмет");

        UserBookingDto userBookingDto = new UserBookingDto();
        userBookingDto.setId(1L);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.of(2023,5,5,1,10));
        bookingDto.setEnd(LocalDateTime.of(2023,5,10,1,10));
        bookingDto.setItem(itemBookingDto);
        bookingDto.setBooker(userBookingDto);
        bookingDto.setStatus(BookingStatus.WAITING);
        return bookingDto;
    }


}
