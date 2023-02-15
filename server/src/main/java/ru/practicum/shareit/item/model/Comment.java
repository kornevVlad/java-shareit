package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id; // id комментария

    private String text; //описание комментария

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private User author; //автор комментария

    @ManyToOne()
    @JoinColumn(name = "item_id")
    private Item item; //предмет о котором пишется комментарий

    private LocalDateTime created; //дата и время создания комментария
}