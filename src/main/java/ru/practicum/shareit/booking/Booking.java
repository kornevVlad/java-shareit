package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
public class Booking {

    private Long id;

    private LocalDateTime start; //дата и время начала бронирования;

    private LocalDateTime end; //дата и время конца бронирования;

    private Item item; //вещь, которую пользователь бронирует;

    private User booker; //пользователь, который осуществляет бронирование;

    private String status; //статус бронирования

}
