package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;

    @Email(message = "Email не корректен")
    @NotBlank(message = "Email не может быть пустым")
    private String email;
}
