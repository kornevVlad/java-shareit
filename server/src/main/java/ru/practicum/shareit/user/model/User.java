package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.persistence.*;
/**import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
*/
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "email")
    //@Email(message = "Email не корректен")
    //@NotBlank(message = "Email не может быть пустым")
    private String email;
}