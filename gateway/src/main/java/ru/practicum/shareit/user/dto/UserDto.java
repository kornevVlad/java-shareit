package ru.practicum.shareit.user.dto;

import lombok.Data;
import ru.practicum.shareit.validation_interface.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым", groups = Create.class)
    private String name;

    @Email(message = "Email не корректен", groups = Create.class)
    @NotBlank(message = "Email не может быть пустым", groups = Create.class)
    private String email;
}
