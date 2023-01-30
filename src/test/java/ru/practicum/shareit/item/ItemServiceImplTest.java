package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.status.ItemStatus;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationNotFound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;



@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private UserMapper userMapper;

    private Comment comment;

    @Test
    void createItem() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        assertEquals(itemDto.getId(), getItemDto().getId());
        assertEquals(itemDto.getName(), getItemDto().getName());
        assertEquals(itemDto.getDescription(), getItemDto().getDescription());
    }

    @Test
    void updateItem() {
        UserDto userDto = userService.createUser(getUserDto());
        itemService.createItem(getItemDto(), userDto.getId());

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setName("Вещь");
        itemDto1.setDescription("Вещь вещь");
        itemDto1.setAvailable(ItemStatus.TRUE);

        ItemDto upItemDto = itemService.updateItem(itemDto1, 1L, userDto.getId());
        assertEquals(upItemDto.getId(), 1L);
        assertEquals(upItemDto.getName(), itemDto1.getName());

        assertThrows(ValidationNotFound.class, () ->
                itemService.updateItem(itemDto1, 1L, 20L));
        assertThrows(ValidationNotFound.class, () ->
                itemService.updateItem(itemDto1, 20L, userDto.getId()));

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setName("Вещь");
        itemDto2.setDescription("Вещь вещь");
        itemDto2.setAvailable(ItemStatus.TRUE);

        UserDto userDto1 = new UserDto();
        userDto1.setName("Valentin");
        userDto1.setEmail("valentin@yandex.ru");
        UserDto userDto2 = userService.createUser(userDto1);
        itemService.createItem(itemDto2, userDto2.getId());

        assertThrows(ValidationNotFound.class, () ->
                itemService.updateItem(itemDto2, 2L, userDto.getId()));
    }

    private ItemDto getItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Предмет");
        itemDto.setDescription("Предмет предмет");
        itemDto.setAvailable(ItemStatus.TRUE);
        return itemDto;
    }

    private UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("Vlad");
        userDto.setEmail("vlad@yandex.ru");
        return userDto;
    }
}
