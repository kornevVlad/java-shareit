package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    private UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("vlad");
        userDto.setEmail("vl@ya.ru");
        return userDto;
    }

    @Test
    void goodTest() throws IOException {
        JsonContent<UserDto> result = json.write(getUserDto());

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("vlad");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("vl@ya.ru");
    }
}
