package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class ItemDto {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private Boolean available; //статус доступа предмета

    private LastAndNextBookingDto lastBooking;

    private LastAndNextBookingDto nextBooking;

    private Long requestId; // id ссылки запроса другого пользователя

    private List<CommentDto> comments; //комментарии предмета
}