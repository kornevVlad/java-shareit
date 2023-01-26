package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findBookingByBookerIdOrderByStartDesc(Long bookerId); //ALL ByBookerId

    Page<Booking> findBookingByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable); //ALL ByBookerId PAGE

    List<Booking> findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime time); //FUTURE ByBookerId

    Page<Booking> findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime time, Pageable pageable); //FUTURE ByBookerId PAGE

    List<Booking> findBookingsByBookerIdAndEndIsBefore(
            Long bookerId, LocalDateTime time); //PAST ByBookerId

    Page<Booking> findBookingsByBookerIdAndEndIsBefore(
            Long bookerId, LocalDateTime time, Pageable pageable); //PAST ByBookerId PAGE

    List<Booking> findBookingsByBookerIdAndStatusEquals(
            Long bookerId, BookingStatus status); //WAITING and REJECTED ByBookerId

    Page<Booking> findBookingsByBookerIdAndStatusEquals(
            Long bookerId, BookingStatus status, Pageable pageable); //WAITING and REJECTED ByBookerId PAGE

    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2); //CURRENT ByBookerId

    Page<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2, Pageable pageable); //CURRENT ByBookerId PAGE

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long ownerId); //ALL ByBookerItems

    Page<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable); //ALL ByBookerItems PAGE

    List<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2); //CURRENT ByBookerItems

    Page<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2, Pageable pageable); //CURRENT ByBookerItems PAGE

    List<Booking> findBookingsByItemOwnerIdAndEndIsBefore(
            Long ownerId, LocalDateTime time); //PAST ByBookerItems

    Page<Booking> findBookingsByItemOwnerIdAndEndIsBefore(
            Long ownerId, LocalDateTime time, Pageable pageable); //PAST ByBookerItems PAGE

    List<Booking> findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            Long ownerId, LocalDateTime time); //FUTURE ByBookerItems

    Page<Booking> findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            Long ownerId, LocalDateTime time, Pageable pageable); //FUTURE ByBookerItems PAGE

    List<Booking> findBookingsByItemOwnerIdAndStatusEquals(
            Long ownerId, BookingStatus status); //WAITING and REJECTED ByBookerItems

    Page<Booking> findBookingsByItemOwnerIdAndStatusEquals(
            Long ownerId, BookingStatus status, Pageable pageable); //WAITING and REJECTED ByBookerItems PAGE

    Booking findFirstByItemIdAndEndIsBeforeOrderByEndDesc(
            Long itemId, LocalDateTime time); //LASTBOOKING

    Booking findFirstByItemIdAndStartIsAfter(
            Long itemId, LocalDateTime time); //NEXTBOOKING

    Booking findFirstByItemIdAndBookerIdAndEndIsBefore(
            Long itemId, Long userId, LocalDateTime time);//Бронировал пользователь данный предмет
}