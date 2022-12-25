package ru.practicum.shareit.booking;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Booking {

    private int id;

    private LocalDateTime start; //дата и время начала бронирования;

    private LocalDateTime end; //дата и время конца бронирования;

    private int item; //вещь, которую пользователь бронирует;

    private int booker; //пользователь, который осуществляет бронирование;

    private String status; //статус бронирования

}
