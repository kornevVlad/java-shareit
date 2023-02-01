package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.ValidationBadRequest;
import ru.practicum.shareit.validation.ValidationNotFound;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestServiceImplTest {

    @Autowired
    private final ItemRequestServiceImpl itemRequestService;

    @Autowired
    private final UserService userService;

    private UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("Vlad");
        userDto.setEmail("vl@ya.ru");
        return userDto;
    }

    private ItemRequestDto getItemRequestDto() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Предмет");
        return itemRequestDto;
    }

    @Test
    void createItemRequest() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(
                getItemRequestDto(), userDto.getId(), LocalDateTime.now());
        assertEquals(itemRequestDto.getId(), 1L);
        assertEquals(itemRequestDto.getDescription(), getItemRequestDto().getDescription());
    }

    @Test
    void getItemRequestsByRequestorId() {
        UserDto userDto = userService.createUser(getUserDto());
        itemRequestService.createItemRequest(
                getItemRequestDto(), userDto.getId(), LocalDateTime.now());
        List<ItemRequestDto> itemRequestDtoList = itemRequestService
                .getItemRequestsByRequestorId(userDto.getId());
        assertEquals(itemRequestDtoList.size(), 1);
    }

    @Test
    void getItemRequestById() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemRequestDto itemRequestDto = itemRequestService.createItemRequest(
                getItemRequestDto(), userDto.getId(), LocalDateTime.now());
        ItemRequestDto itemRequestDto1 = itemRequestService.getItemRequestById(
                itemRequestDto.getId(), userDto.getId());
        assertEquals(itemRequestDto1.getId(), itemRequestDto.getId());
        assertEquals(itemRequestDto1.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    void getAllItemRequests() {
        UserDto userDto = userService.createUser(getUserDto());
        itemRequestService.createItemRequest(
                getItemRequestDto(), userDto.getId(), LocalDateTime.now());
        List<ItemRequestDto> itemRequestDtoList = itemRequestService
                .getAllItemRequests(userDto.getId(), 1,1);
        assertEquals(itemRequestDtoList.size(), 0);
    }

    @Test
    void getAllItemRequestsValidation() {
        UserDto userDto = userService.createUser(getUserDto());
        itemRequestService.createItemRequest(
                getItemRequestDto(), userDto.getId(), LocalDateTime.now());

        assertThrows(ValidationBadRequest.class, () -> itemRequestService
                .getAllItemRequests(userDto.getId(), -1,1));

        assertThrows(ValidationBadRequest.class, () -> itemRequestService
                .getAllItemRequests(userDto.getId(), 1,-1));

        assertThrows(ValidationBadRequest.class, () -> itemRequestService
                .getAllItemRequests(userDto.getId(), 1,0));

        assertThrows(ValidationBadRequest.class, () -> itemRequestService
                .getAllItemRequests(userDto.getId(), 10,2));
    }

    @Test
    void validUser() {
        assertThrows(ValidationNotFound.class, () -> itemRequestService.createItemRequest(
                getItemRequestDto(), 10L, LocalDateTime.now()));
    }

    @Test
    void validItemRequest() {
        UserDto userDto = userService.createUser(getUserDto());
        itemRequestService.createItemRequest(
                getItemRequestDto(), userDto.getId(), LocalDateTime.now());

        assertThrows(ValidationNotFound.class, () -> itemRequestService.getItemRequestById(
                10L, userDto.getId()));
    }
}