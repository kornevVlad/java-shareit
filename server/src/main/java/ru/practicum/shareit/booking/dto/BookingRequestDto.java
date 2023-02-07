package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    private Long itemId;

    private LocalDateTime start; //дата и время начала бронирования;

    private LocalDateTime end; //дата и время конца бронирования;
}