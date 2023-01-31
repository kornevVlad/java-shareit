package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.ExceptionState;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
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

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceImplTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Test
    void createBooking() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        BookingDto bookingDto = bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());
        assertEquals(bookingDto.getId(), 1L);
    }

    @Test
    void createBookingValid() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);
        bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());

        assertThrows(ValidationNotFound.class, () -> bookingService.createBooking(
                getBookingRequestDto(2L), userDto2.getId()));

        assertThrows(ValidationNotFound.class, () -> bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), 3L));

        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemDto.getId());
        bookingRequestDto.setStart(LocalDateTime.of(2023,2,3,1,1));
        bookingRequestDto.setEnd(LocalDateTime.of(2023,1,3,1,1));

        assertThrows(ValidationBadRequest.class, () -> bookingService.createBooking(
                bookingRequestDto, userDto2.getId()));

        assertThrows(ValidationNotFound.class, () ->  bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto.getId()));
    }

    @Test
    void updateBooking() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);
        BookingDto bookingDto = bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());

        BookingDto bookingDto1 = bookingService.updateBooking(
                bookingDto.getId(), userDto.getId(), true);
        assertEquals(bookingDto1.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    void updateBookingValid() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);
        BookingDto bookingDto = bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());

        assertThrows(ValidationNotFound.class, () -> bookingService.updateBooking(
                bookingDto.getId(), userDto2.getId(), true));

        bookingService.updateBooking(
                bookingDto.getId(), userDto.getId(), true);

        assertThrows(ValidationBadRequest.class, () -> bookingService.updateBooking(
                bookingDto.getId(), userDto.getId(), true));

    }

    @Test
    void getBooking() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        BookingDto bookingDto = bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());

        BookingDto bookingDto1 = bookingService.getBooking(bookingDto.getId(), userDto.getId());
        assertEquals(bookingDto.getBooker(), bookingDto1.getBooker());
    }

    @Test
    void getBookingsByBookerId() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());

        List<BookingDto> bookingDtos = bookingService.getBookingsByBookerId(
                userDto2.getId(), "ALL",1,1);
        assertEquals(bookingDtos.size(),1);

        List<BookingDto> bookingDtos1 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "ALL",1,null);
        assertEquals(bookingDtos1.size(),1);

        assertThrows(ExceptionState.class, () -> bookingService.getBookingsByBookerId(
                userDto2.getId(), "HELLO",1,null));

    }

    @Test
    void getBookingsByBookerItems() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        BookingDto bookingDto = bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());
        List<BookingDto> bookingDtos = bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", 1, 1);
        assertEquals(bookingDtos.size(), 1);

        List<BookingDto> bookingDtos1 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", 1, null);
        assertEquals(bookingDtos1.size(), 1);
    }

    private UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setName("VL");
        userDto.setEmail("vl@ya.ru");
        return userDto;
    }

    private ItemDto getItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Предмет");
        itemDto.setDescription("Предмет предмет");
        itemDto.setAvailable(ItemStatus.TRUE);
        return itemDto;
    }

    private BookingRequestDto getBookingRequestDto(Long itemId) {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setItemId(itemId);
        bookingRequestDto.setStart(LocalDateTime.now());
        bookingRequestDto.setEnd(LocalDateTime.now().plusSeconds(1));
        return bookingRequestDto;
    }
}
