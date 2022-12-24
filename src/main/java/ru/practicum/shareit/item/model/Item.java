package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {

    private int id;

    private String name;

    private String description;

    private Boolean available; //статус о том, доступна или нет вещь для аренды

    private int owner; //владелец вещи;

    private String request; //хранинение ссылки запроса другого пользователя
}