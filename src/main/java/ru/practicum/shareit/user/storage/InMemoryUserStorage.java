package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.ValidationEmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private long id = 1;
    protected Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        validationEmail(user);
        user.setId(id);
        users.put(id,user);
        id++;
        log.info("User добавлен {}",users.size());
        return users.get(user.getId());
    }

    @Override
    public User updateUser(User user, Long id) {
        User upUser = users.get(id);
        if ((user.getName() != null)) {
            upUser.setName(user.getName());
        }
        if ((user.getEmail() != null)) {
            validationEmail(user);
            upUser.setEmail(user.getEmail());
        }
        upUser.setId(id);
        users.put(id, upUser);
        log.info("User обновлен {}",upUser);
        return upUser;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
        log.info("User id {} удален", id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        log.info("Размер списка User {}", users.size());
        return users.get(id);
    }

    private User validationEmail(User user) {
        for (User userEmail : users.values()) {
            if (user.getEmail().equals(userEmail.getEmail())) {
                throw new ValidationEmail("email уже существует");
            }
        }
        return user;
    }
}