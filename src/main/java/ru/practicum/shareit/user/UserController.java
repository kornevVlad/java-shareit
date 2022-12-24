package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping() //создание пользователя
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Post User {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}") //обновление пользователя
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable int userId) {
        log.info("Patch User {}, userId {}", userDto, userId);
        return userService.updateUser(userDto, userId);
    }

    @GetMapping() //вывод списка пользователей
    public List<UserDto> getUsers(){
        log.info("Get Users");
        return userService.getUsers();
    }

    @GetMapping("/{userId}") //получение пользователя по id
    public UserDto getUsersById(@PathVariable int userId){
        log.info("Get UserById {}",userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}") //удаление пользователя
    public void deleteUser(@PathVariable int userId) {
        log.info("Delete User {}", userId);
        userService.deleteUser(userId);
    }
}