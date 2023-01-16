package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long id;

    private LocalDateTime start; //дата и время начала бронирования;

    private LocalDateTime end; //дата и время конца бронирования;

    private ItemBookingDto item; //вещь, которую пользователь бронирует;

    private UserBookingDto booker; //пользователь, который осуществляет бронирование;

    private BookingStatus status; //статус бронирования
}