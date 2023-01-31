package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.exception.ExceptionState;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.status.ItemStatus;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.validation.ValidationBadRequest;
import ru.practicum.shareit.validation.ValidationNotFound;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              BookingMapper bookingMapper,
                              UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingDto, Long bookerId) {
        validItemById(bookingDto.getItemId());
        validTime(bookingDto.getStart(), bookingDto.getEnd());
        validUserById(bookerId);
        Item item = itemRepository.getReferenceById(bookingDto.getItemId());
        User booker = userRepository.getReferenceById(bookerId);
        if (item.getOwner().getId().equals(bookerId)) {
            log.error("Владелец предмета не может бронировать собственный предмет");
            throw new ValidationNotFound("NOT FOUND");
        }
        Booking booking = bookingMapper.toBooking(bookingDto, booker, item);
        validStatusBooking(booking);

        log.info("Бронирование :  {}", booking);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto updateBooking(Long bookingId, Long userId, Boolean approved) {
        validBookingById(bookingId);
        validUserById(userId);
        Booking booking = bookingRepository.getReferenceById(bookingId);

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            log.error("Пользователь с ID {} не является владельцем предмета", userId);
            throw new ValidationNotFound("NOT FOUND");
        }
        if (booking.getStatus() == BookingStatus.APPROVED) {
            log.error("Невозможно изменить APPROVED на APPROVED");
            throw new ValidationBadRequest("BAD REQUEST");
        }
            if (approved == true) {
                booking.setStatus(BookingStatus.APPROVED);
            }
            if (approved == false) {
                booking.setStatus(BookingStatus.REJECTED);
            }

        bookingRepository.save(booking);
        log.info("Обновлен статус бронирования {}", booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        validBookingById(bookingId);
        validUserById(userId);

        Booking booking = bookingRepository.getReferenceById(bookingId);
        validBookingByUserId(booking, userId);
        log.info("get бронирование {}", booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long bookerId, String state, Integer from, Integer size) {
        validUserById(bookerId);
        if (size != null) {
            validFromAndSize(from, size);
            Sort sort = Sort.by(Sort.Direction.DESC, "start");
            Pageable pageable = validPageable(from,size,sort);
            return getBookingsByBookerIdPagination(bookerId, state, pageable);
        }
        return getBookingsByBookerIdNotPagination(bookerId, state);
    }

    @Override
    public List<BookingDto> getBookingsByBookerItems(Long ownerId, String state, Integer from, Integer size) {
        validUserById(ownerId);
        if (size != null) {
            validFromAndSize(from, size);
            Sort sort = Sort.by(Sort.Direction.DESC, "start");
            Pageable pageable = validPageable(from,size,sort);
            return getBookingsByBookerItemsPagination(ownerId, state, pageable);
        }
        return getBookingsByBookerItemsNotPagination(ownerId, state);
    }

    private void validUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.error("Пользователь с id {} не найден", userId);
            throw new ValidationNotFound("Пользователь не найден");
        }
    }

    private void validBookingById(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            log.error("Бронирование с id {} не найдено", bookingId);
            throw new ValidationNotFound("Бронирование не найдено");
        }
    }

    private void validTime(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            log.error("Совпадение время start = {} и end = {}",start, end);
            throw new ValidationBadRequest("BAD REQUEST");
        }
    }

    private void validStatusBooking(Booking booking) {
        if (booking.getItem().getAvailable() == ItemStatus.FALSE) {
            log.error("Статус арендованного предмета {} с id {}",
                    booking.getItem().getAvailable(), booking.getItem().getId());
            throw new ValidationBadRequest("BAD REQUEST");
        }
    }

    private void validBookingByUserId(Booking booking, Long userId) {
        if (!booking.getItem().getOwner().getId().equals(userId) &&
                !booking.getBooker().getId().equals(userId)) {
            throw new ValidationNotFound("NOT FOUND USER ID" + userId);
        }
    }

    private void validItemById(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            log.error("Предмет с id {} не найден", itemId);
            throw new ValidationNotFound("Предмет с таким id не найден");
        }
    }

    private List<BookingDto> getBookingsByBookerIdNotPagination(Long bookerId, String state) {
        switch (state) {
            case "ALL":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingByBookerIdOrderByStartDesc(bookerId));
            case "PAST":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByBookerIdAndEndIsBefore(bookerId, LocalDateTime.now()));

            case "WAITING":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByBookerIdAndStatusEquals(bookerId, BookingStatus.WAITING));

            case "REJECTED":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByBookerIdAndStatusEquals(bookerId, BookingStatus.REJECTED));

            case "FUTURE":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
                                bookerId, LocalDateTime.now()));

            case "CURRENT":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(
                                bookerId, LocalDateTime.now(),LocalDateTime.now()));
            default:
                throw new ExceptionState("Unknown state: " + state);
        }
    }

    private List<BookingDto> getBookingsByBookerIdPagination(Long bookerId, String state, Pageable pageable) {
        Page<Booking> bookingsPage;
        switch (state) {
            case "ALL":
                bookingsPage = bookingRepository.findBookingByBookerIdOrderByStartDesc(bookerId, pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);
            case "PAST":
                bookingsPage = bookingRepository.findBookingsByBookerIdAndEndIsBefore(
                        bookerId, LocalDateTime.now(), pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "WAITING":
                bookingsPage = bookingRepository.findBookingsByBookerIdAndStatusEquals(
                        bookerId, BookingStatus.WAITING, pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "REJECTED":
                bookingsPage = bookingRepository.findBookingsByBookerIdAndStatusEquals(
                        bookerId, BookingStatus.REJECTED, pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "FUTURE":
                bookingsPage = bookingRepository.findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
                        bookerId, LocalDateTime.now(), pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "CURRENT":
                bookingsPage = bookingRepository.findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(
                                bookerId, LocalDateTime.now(),LocalDateTime.now(), pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            default:
                throw new ExceptionState("Unknown state: " + state);
        }
    }

    public List<BookingDto> getBookingsByBookerItemsNotPagination(Long ownerId, String state) {
        switch (state) {
            case "ALL":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(ownerId));

            case "CURRENT":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                                ownerId, LocalDateTime.now(), LocalDateTime.now()));

            case "PAST":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByItemOwnerIdAndEndIsBefore(
                                ownerId, LocalDateTime.now()));

            case "FUTURE":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                                ownerId, LocalDateTime.now()));

            case "WAITING":
                return bookingMapper.toBookingDtoList(
                        bookingRepository.findBookingsByItemOwnerIdAndStatusEquals(
                                ownerId, BookingStatus.WAITING));

            case "REJECTED":
                return bookingMapper.toBookingDtoList(bookingRepository.findBookingsByItemOwnerIdAndStatusEquals(
                        ownerId, BookingStatus.REJECTED));

            default:
                throw new ExceptionState("Unknown state: " + state);
        }
    }

    private List<BookingDto> getBookingsByBookerItemsPagination(Long ownerId, String state, Pageable pageable) {
        Page<Booking> bookingsPage;
        switch (state) {
            case "ALL":
                bookingsPage = bookingRepository.findBookingsByItemOwnerIdOrderByStartDesc(ownerId, pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "CURRENT":
                bookingsPage = bookingRepository.findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
                        ownerId, LocalDateTime.now(), LocalDateTime.now(), pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "PAST":
                bookingsPage = bookingRepository.findBookingsByItemOwnerIdAndEndIsBefore(
                        ownerId, LocalDateTime.now(), pageable);
                return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "FUTURE":
                      bookingsPage = bookingRepository.findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
                              ownerId, LocalDateTime.now(), pageable);
                      return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "WAITING":
                      bookingsPage = bookingRepository.findBookingsByItemOwnerIdAndStatusEquals(
                                ownerId, BookingStatus.WAITING, pageable);
                      return bookingMapper.toBookingPageDtoList(bookingsPage);

            case "REJECTED":
               bookingsPage = bookingRepository.findBookingsByItemOwnerIdAndStatusEquals(
                        ownerId, BookingStatus.REJECTED, pageable);
               return bookingMapper.toBookingPageDtoList(bookingsPage);

            default:
                throw new ExceptionState("Unknown state: " + state);
        }
    }


    private void validFromAndSize(Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationBadRequest("Индекс эллемента не может быть меньше нуля");
        }
        if (size < 0) {
            throw new ValidationBadRequest("Размер не может быть меньше нуля");
        }
        if (size == 0) {
            throw new ValidationBadRequest("Размер списка не может быть нулевым");
        }
        if (from > size) {
            throw new ValidationBadRequest("from не может быть больше size");
        }
    }

    private Pageable validPageable(Integer from, Integer size, Sort sort) {
        Pageable page;
        if (from.equals(size)) {
            page = PageRequest.of(from - 1, size, sort);
            return page;
        }
        page = PageRequest.of(from, size, sort);
        return page;
    }
}