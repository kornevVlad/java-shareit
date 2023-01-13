package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long id;

    private LocalDateTime start; //дата и время начала бронирования;

    private LocalDateTime end; //дата и время конца бронирования;

    private Item item; //вещь, которую пользователь бронирует;

    private User booker; //пользователь, который осуществляет бронирование;

    private BookingStatus status; //статус бронирования
}