package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;


    @Test
    void createUser() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(getUserDto());

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(getUserDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(getUserDto().getId()), Long.class))
                .andExpect(jsonPath("$.name", is(getUserDto().getName())))
                .andExpect(jsonPath("$.email", is(getUserDto().getEmail())));
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(any(), any(Long.class)))
                .thenReturn(getUserDto());

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(getUserDto()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(getUserDto().getId()), Long.class))
                .andExpect(jsonPath("$.name", is(getUserDto().getName())))
                .andExpect(jsonPath("$.email", is(getUserDto().getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUserById(any(Long.class)))
                .thenReturn(getUserDto());

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(getUserDto().getId()), Long.class))
                .andExpect(jsonPath("$.name", is(getUserDto().getName())))
                .andExpect(jsonPath("$.email", is(getUserDto().getEmail())));
    }

    @Test
    void getUsers() throws Exception {
        List<UserDto> usersDto = new ArrayList<>();
        usersDto.add(getUserDto());

        when(userService.getUsers())
                .thenReturn(usersDto);
        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(usersDto)));
    }

    private UserDto getUserDto() {
        Long id = 1L;
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName("Vlad");
        userDto.setEmail("vlad@yandex.ru");
        return userDto;
    }
}
