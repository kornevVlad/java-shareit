package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user, int id);

    void deleteUser(int id);

    List<User> getUsers();

    User getUserById(int id);
}
