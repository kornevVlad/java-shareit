package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationNotFound;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceImplTest {

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private UserMapper userMapper;

    @Test
    void createUser() {
        Long id = 1L;
        User userTest = getUser(id);
        UserDto userDto = userServiceImpl.createUser(userMapper.toUserDto(userTest));
        assertEquals(userDto.getId(), userTest.getId());
        assertEquals(userDto.getName(), userTest.getName());
        assertEquals(userDto.getEmail(), userTest.getEmail());
    }

    @Test
    void updateUser() {
        Long id = 1L;
        User userTest = getUser(id);
        userServiceImpl.createUser(userMapper.toUserDto(userTest));

        User upUser = new User();
        upUser.setName("Valentin");
        upUser.setEmail("valentin@ya.ru");

        UserDto userDto = userServiceImpl.updateUser(userMapper.toUserDto(upUser), id);
        assertEquals(userDto.getId(), id);
        assertEquals(userDto.getName(), upUser.getName());
        assertEquals(userDto.getEmail(), upUser.getEmail());
    }

    @Test
    void deleteUser() {
        Long id = 1L;
        User userTest = getUser(id);
        userServiceImpl.createUser(userMapper.toUserDto(userTest));
        UserDto userDto = userServiceImpl.getUserById(id);

        assertEquals(userDto.getId(), id);
        userServiceImpl.deleteUser(id);
        assertThrows(ValidationNotFound.class, () -> {userServiceImpl.getUserById(id);});
    }

    @Test
    void getUsersSize() {
        Long id = 1L;
        User userTest = getUser(id);
        userServiceImpl.createUser(userMapper.toUserDto(userTest));
        List<UserDto> users = userServiceImpl.getUsers();
        assertEquals(users.size(), 1);
    }

    private User getUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("Vlad");
        user.setEmail("vlad@yandex.ru");
        return user;
    }
}
