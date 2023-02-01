package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingRequestDtoTest {

    @Autowired
    private JacksonTester<BookingRequestDto> json;

    private BookingRequestDto getBookingRequestDto() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(1L);
        bookingRequestDto.setStart(LocalDateTime.of(2023,10,10,1,10));
        bookingRequestDto.setEnd(LocalDateTime.of(2023,11,10,1,10));
        return bookingRequestDto;
    }

    @Test
    void goodTest() throws IOException {
        JsonContent<BookingRequestDto> result = json.write(getBookingRequestDto());
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    }
}
