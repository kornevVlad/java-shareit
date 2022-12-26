package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.status.ItemStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class ItemDto {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private ItemStatus available; //статус доступа предмета

    private Long requestId; // id ссылки запроса другого пользователя
}