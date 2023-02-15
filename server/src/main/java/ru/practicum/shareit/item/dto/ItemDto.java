package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.status.ItemStatus;

import java.util.List;


@Data
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private ItemStatus available; //статус доступа предмета

    private LastAndNextBookingDto lastBooking;

    private LastAndNextBookingDto nextBooking;

    private Long requestId; // id ссылки запроса другого пользователя

    private List<CommentDto> comments; //комментарии предмета
}