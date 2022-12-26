package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return userMapper.toUserDto(userStorage.createUser(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        return userMapper.toUserDto(userStorage.updateUser(userMapper.toUser(userDto),id));
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }

    @Override
    public List<UserDto> getUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : userStorage.getUsers()) {
            usersDto.add(userMapper.toUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.toUserDto(userStorage.getUserById(id));
    }
}