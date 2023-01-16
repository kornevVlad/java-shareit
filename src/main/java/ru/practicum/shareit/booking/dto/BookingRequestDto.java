package ru.practicum.shareit.booking.dto;

import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingRequestDto {

    @NotNull
    private Long itemId;

    @FutureOrPresent
    private LocalDateTime start; //дата и время начала бронирования;

    @FutureOrPresent
    private LocalDateTime end; //дата и время конца бронирования;
}
