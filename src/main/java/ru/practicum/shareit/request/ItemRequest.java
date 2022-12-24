package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {

    private int id;

    private String description;

    private int requestor; //пользователь, создавший запрос

    private LocalDateTime created; //дата и время создания запроса
}