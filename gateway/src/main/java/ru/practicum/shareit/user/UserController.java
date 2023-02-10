package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation_interface.Create;
import ru.practicum.shareit.validation_interface.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping() //создание пользователя
    public ResponseEntity<Object> createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Post User {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}") //обновление пользователя
    public ResponseEntity<Object> updateUser(@Validated(Update.class) @RequestBody UserDto userDto,
                                             @Min(value = 1) @PathVariable Long userId) {
        log.info("Patch User {}, userId {}", userDto, userId);
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping() //вывод списка пользователей
    public ResponseEntity<Object> getUsers() {
        log.info("Get Users");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}") //получение пользователя по id
    public ResponseEntity<Object> getUsersById(@NotNull @Min(value = 1) @PathVariable Long userId) {
        log.info("Get UserById {}",userId);
        return userClient.getUserById(userId);
    }

    @DeleteMapping("/{userId}") //удаление пользователя
    public ResponseEntity<Object> deleteUser(@NotNull @Min(value = 1) @PathVariable Long userId) {
        log.info("Delete User {}", userId);
        return userClient.deleteUserById(userId);
    }
}