package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.validation_interface.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RequestDto {

    private Long id;

    @NotNull(groups = Create.class)
    @NotEmpty(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String description;
}
