package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserClient userClient;

    @PostMapping() //создание пользователя
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Post User {}", userDto);
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}") //обновление пользователя
    public ResponseEntity<Object> updateUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Patch User {}, userId {}", userDto, userId);
        return userClient.updateUser(userDto, userId);
    }

    @GetMapping() //вывод списка пользователей
    public ResponseEntity<Object> getUsers() {
        log.info("Get Users");
        return userClient.getUsers();
    }

    @GetMapping("/{userId}") //получение пользователя по id
    public ResponseEntity<Object> getUsersById(@PathVariable Long userId) {
        log.info("Get UserById {}",userId);
        return userClient.getUserById(userId);
    }

    @DeleteMapping("/{userId}") //удаление пользователя
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        log.info("Delete User {}", userId);
        return userClient.deleteUserById(userId);
    }
}

