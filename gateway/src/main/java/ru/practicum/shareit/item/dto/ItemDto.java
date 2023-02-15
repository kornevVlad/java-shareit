package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validation_interface.Create;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
public class ItemDto {

    private Long id;

    @NotEmpty(groups = Create.class)
    private String name;

    @NotEmpty(groups = Create.class)
    private String description;

    @NotNull(groups = Create.class)
    private Boolean available; //статус доступа предмета

    private Long requestId; // id ссылки запроса другого пользователя

    private List<CommentDto> comments; //комментарии предмета
}
