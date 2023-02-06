package ru.practicum.shareit.request.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RequestDto {

    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    private String description;
}
