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
    void getBookingValidBookingId() {
        assertThrows(ValidationNotFound.class, () -> bookingService.getBooking(1L, 1L));
    }

    @Test
    void getBookingsByBookerIdNotNullPagination() {
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

        List<BookingDto> bookingDtos2 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "WAITING",1,1);
        assertEquals(bookingDtos2.size(),0);

        List<BookingDto> bookingDtos3 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "FUTURE",1,2);
        assertEquals(bookingDtos3.size(),0);

       /** List<BookingDto> bookingDtos4 = bookingService.getBookingsByBookerId(
        *       userDto2.getId(), "CURRENT",1,3);
       * assertEquals(bookingDtos4.size(),1);*/

        List<BookingDto> bookingDtos5 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "PAST",1,2);
        assertEquals(bookingDtos5.size(),0);

        List<BookingDto> bookingDtos6 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "REJECTED",1,2);
        assertEquals(bookingDtos6.size(),0);


        assertThrows(ExceptionState.class, () -> bookingService.getBookingsByBookerId(
                userDto2.getId(), "HELLO",1,2));
    }

    @Test
    void getBookingsByBookerIdNullPagination() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());

        List<BookingDto> bookingDtos1 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "ALL",1,null);
        assertEquals(bookingDtos1.size(),1);

        List<BookingDto> bookingDtos2 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "WAITING",1,null);
        assertEquals(bookingDtos2.size(),1);

        List<BookingDto> bookingDtos3 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "FUTURE",1,null);
        assertEquals(bookingDtos3.size(),0);

        List<BookingDto> bookingDtos4 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "CURRENT",1,null);
        assertEquals(bookingDtos4.size(),1);

        List<BookingDto> bookingDtos5 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "PAST",1,null);
        assertEquals(bookingDtos5.size(),0);

        List<BookingDto> bookingDtos6 = bookingService.getBookingsByBookerId(
                userDto2.getId(), "REJECTED",1,null);
        assertEquals(bookingDtos6.size(),0);


        assertThrows(ExceptionState.class, () -> bookingService.getBookingsByBookerId(
                userDto2.getId(), "HELLO",1,null));
    }

    @Test
    void getBookingsByBookerItemsPagination() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());
        List<BookingDto> bookingDtos = bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", 1, 1);
        assertEquals(bookingDtos.size(), 0);

        List<BookingDto> bookingDtos1 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "WAITING", 1, 1);
        assertEquals(bookingDtos1.size(), 0);

        List<BookingDto> bookingDtos2 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "PAST", 1, 1);
        assertEquals(bookingDtos2.size(), 0);

        List<BookingDto> bookingDtos3 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "FUTURE", 1, 1);
        assertEquals(bookingDtos3.size(), 0);

        List<BookingDto> bookingDtos4 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "REJECTED", 1, 1);
        assertEquals(bookingDtos4.size(), 0);

        assertThrows(ExceptionState.class, () -> bookingService.getBookingsByBookerId(
                userDto2.getId(), "HELLO",1,1));
    }

    @Test
    void getBookingsByBookerItemsNullPagination() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);

        bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());
        List<BookingDto> bookingDtos = bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", 1, null);
        assertEquals(bookingDtos.size(), 1);

        List<BookingDto> bookingDtos1 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "WAITING", 1, null);
        assertEquals(bookingDtos1.size(), 1);

        List<BookingDto> bookingDtos2 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "PAST", 1, null);
        assertEquals(bookingDtos2.size(), 0);

        List<BookingDto> bookingDtos3 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "FUTURE", 1, null);
        assertEquals(bookingDtos3.size(), 0);

        List<BookingDto> bookingDtos4 = bookingService.getBookingsByBookerItems(
                userDto.getId(), "REJECTED", 1, null);
        assertEquals(bookingDtos4.size(), 0);

        assertThrows(ExceptionState.class, () -> bookingService.getBookingsByBookerId(
                userDto2.getId(), "HELLO",1,null));
    }

    @Test
    void validFromAndSize() {
        UserDto userDto = userService.createUser(getUserDto());
        ItemDto itemDto = itemService.createItem(getItemDto(), userDto.getId());
        UserDto userDto1 = new UserDto();
        userDto1.setName("ED");
        userDto1.setEmail("ed@ya.ru");
        UserDto userDto2 = userService.createUser(userDto1);
        bookingService.createBooking(
                getBookingRequestDto(itemDto.getId()), userDto2.getId());

        assertThrows(ValidationBadRequest.class, () -> bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", -1, 1));

        assertThrows(ValidationBadRequest.class, () -> bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", 1, -1));

        assertThrows(ValidationBadRequest.class, () -> bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", 1, 0));

        assertThrows(ValidationBadRequest.class, () -> bookingService.getBookingsByBookerItems(
                userDto.getId(), "ALL", 10, 2));
    }
}
