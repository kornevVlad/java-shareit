package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.validation_interface.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    @NotEmpty(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String text;

    private String authorName;

    private LocalDateTime created;
}
