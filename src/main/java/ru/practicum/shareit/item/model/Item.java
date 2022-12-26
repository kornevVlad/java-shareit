package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.item.status.ItemStatus;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class Item {

    private Long id;

    private String name;

    private String description;

    private ItemStatus available; //статус о том, доступна или нет вещь для аренды

    private Long owner; //владелец вещи;

    private ItemRequest request; //хранинение ссылки запроса другого пользователя
}