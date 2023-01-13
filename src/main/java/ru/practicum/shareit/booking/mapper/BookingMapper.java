package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class BookingMapper {
    private final UserService userService;

    private final UserMapper userMapper;

    private final ItemService itemService;

    private final ItemMapper itemMapper;


    public BookingMapper(UserService userService, UserMapper userMapper,
                         ItemService itemService, ItemMapper itemMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    public Booking toBooking(BookingRequestDto bookingDto, Long bookerId){
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(userMapper.toUser(userService.getUserById(bookerId))); //запись пользователя
        booking.setItem(itemMapper.toItem(itemService.getItemById(bookingDto.getItemId()), //запись предмета
                        userMapper.toUser(userService.getUserById(bookerId)))); //запись пользователя
        booking.setStatus(BookingStatus.WAITING);
        return booking;
    }

    public BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public List<BookingDto> toBookingDtoList(List<Booking> bookings) {
        List<BookingDto> bookingsDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingsDto.add(toBookingDto(booking));
        }
        return bookingsDto;
    }
}