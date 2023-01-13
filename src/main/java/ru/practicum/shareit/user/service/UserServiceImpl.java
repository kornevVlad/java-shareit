package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.ValidationNotFound;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        User user = userRepository.getById(id);
        User upUser = userMapper.toUser(userDto);
        if (upUser.getName() != null) {
            user.setName(upUser.getName());
        }
        if (upUser.getEmail() != null) {
            user.setEmail(upUser.getEmail());
        }
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(userMapper.toUserDto(user));
        }
        return usersDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        try {
            return userMapper.toUserDto(userRepository.getById(id));
        } catch (Exception vl) {
            log.info("Пользователь с id {} не найден", id);
            throw new ValidationNotFound(vl.getMessage());
        }
    }
}