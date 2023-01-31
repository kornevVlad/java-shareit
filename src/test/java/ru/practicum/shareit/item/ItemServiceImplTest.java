package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestCommentDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.status.ItemStatus;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationBadRequest;
import ru.practicum.shareit.validation.ValidationNotFound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
    private BookingService bookingService;

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

    @Test
    void getItemById() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());

        ItemDto itemDto1 = itemService.getItemById(itemDto.getId(), userDto.getId());
        assertEquals(itemDto.getId(), itemDto1.getId());
        assertEquals(itemDto.getName(), itemDto1.getName());
    }

    @Test
    void getItems() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        List<ItemDto> items = itemService.getItems(userDto.getId());
        assertEquals(items.size(), 1);
    }

    @Test
    void getItemsBySearch() {
        UserDto userDto = userService.createUser(getUserDto());
        itemService.createItem(getItemDto(), userDto.getId());
        List<ItemDto> items = itemService.getItemsBySearch("Предмет");
        assertEquals(items.size(),1);
        for (ItemDto itemDto : items) {
            assertEquals(itemDto.getName(), getItemDto().getName());
        }
    }

    @Test
    void createComment() throws InterruptedException {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());

        UserDto userDto1 = new UserDto();
        userDto1.setName("Edik");
        userDto1.setEmail("edik@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemDto.getId());
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusSeconds(1));
        BookingDto bookingDto = bookingService.createBooking(bookingRequestDto,userDto2.getId());
        bookingService.updateBooking(bookingDto.getId(),userDto.getId(), true);

        TimeUnit.SECONDS.sleep(2);

        RequestCommentDto requestCommentDto = new RequestCommentDto();
        requestCommentDto.setText("TEXT");
        CommentDto commentDto = itemService.createComment(
                requestCommentDto, itemDto.getId(), userDto2.getId());
        assertEquals(commentDto.getText(), requestCommentDto.getText());

        assertThrows(ValidationBadRequest.class, () -> itemService.createComment(
                requestCommentDto,itemDto.getId(),1L));
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