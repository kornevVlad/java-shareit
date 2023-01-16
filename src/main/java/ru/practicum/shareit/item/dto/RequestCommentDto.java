package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class RequestCommentDto {

    @NotEmpty
    @NotBlank
    String text;
}
