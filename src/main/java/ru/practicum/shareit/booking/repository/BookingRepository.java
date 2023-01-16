package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;


public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findBookingByBookerIdOrderByStartDesc(Long bookerId); //ALL ByBookerId

    List<Booking> findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime time); //FUTURE ByBookerId

    List<Booking> findBookingsByBookerIdAndEndIsBefore(
            Long bookerId, LocalDateTime time); //PAST ByBookerId

    List<Booking> findBookingsByBookerIdAndStatusEquals(
            Long bookerId, BookingStatus status); //WAITING and REJECTED ByBookerId

    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2); //CURRENT ByBookerId

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long ownerId); //ALL ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2); //CURRENT ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndEndIsBefore(
            Long ownerId, LocalDateTime time); //PAST ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            Long ownerId, LocalDateTime time); //FUTURE ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndStatusEquals(
            Long ownerId, BookingStatus status); //WAITING and REJECTED ByBookerItems

    Booking findFirstByItemIdAndEndIsBeforeOrderByEndDesc(
            Long itemId, LocalDateTime time); //LASTBOOKING

    Booking findFirstByItemIdAndStartIsAfter(
            Long itemId, LocalDateTime time); //NEXTBOOKING

    Booking findFirstByItemIdAndBookerIdAndEndIsBefore(
            Long itemId, Long userId, LocalDateTime time);//Бронировал пользователь данный предмет
}