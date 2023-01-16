package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    @NotEmpty
    @NotBlank
    private String text;

    private String authorName;

    private LocalDateTime created;
}
