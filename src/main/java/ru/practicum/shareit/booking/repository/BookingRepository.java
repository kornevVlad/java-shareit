package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findBookingByBookerIdOrderByStartDesc(Long bookerId); //get ALL ByBookerId

    List<Booking> findBookingsByBookerIdAndStartIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime time); // get FUTURE ByBookerId

    List<Booking> findBookingsByBookerIdAndEndIsBefore(
            Long bookerId, LocalDateTime time); //get PAST ByBookerId

    List<Booking> findBookingsByBookerIdAndStatusEquals(
            Long bookerId,  BookingStatus status); //get WAITING and REJECTED ByBookerId
    List<Booking> findBookingByBookerIdAndStartIsBeforeAndEndIsAfter(
            Long bookerId, LocalDateTime time1, LocalDateTime time2); //get CURRENT ByBookerId

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long ownerId); //get ALL ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndStartIsBeforeAndEndIsAfter(
            Long ownerId, LocalDateTime time1, LocalDateTime time2); //get CURRENT ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndEndIsBefore(
            Long ownerId, LocalDateTime time); //get PAST ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            Long ownerId, LocalDateTime time); //get FUTURE ByBookerItems

    List<Booking> findBookingsByItemOwnerIdAndStatusEquals(
            Long ownerId, BookingStatus status); //get WAITING and REJECTED ByBookerItems
}