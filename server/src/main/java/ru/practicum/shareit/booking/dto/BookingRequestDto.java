package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    @NotNull
    private Long itemId;

    private LocalDateTime start; //дата и время начала бронирования;

    private LocalDateTime end; //дата и время конца бронирования;
}