package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {

    private Long id;

    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;

    @Email(message = "Email не корректен")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}