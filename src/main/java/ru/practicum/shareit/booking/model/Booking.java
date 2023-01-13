package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;

import lombok.Data;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;


    @Column(name = "start_date")
   // @FutureOrPresent
    private LocalDateTime start; //дата и время начала бронирования;

    @Column(name = "end_date")
   // @FutureOrPresent
    private LocalDateTime end; //дата и время конца бронирования;

    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item; //вещь, которую пользователь бронирует;

    @ManyToOne()
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    private User booker; //пользователь, который осуществляет бронирование;

    @Enumerated(EnumType.STRING)
    private BookingStatus status; //статус бронирования
}