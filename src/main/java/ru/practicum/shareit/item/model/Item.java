package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.item.status.ItemStatus;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemStatus available; //статус о том, доступна или нет вещь для аренды

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; //владелец вещи;

    @Column(name = "request_id")
    private Long requestId; //хранинение ссылки запроса другого пользователя
}