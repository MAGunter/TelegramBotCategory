package com.test.telegrambot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность пользователя для хранения данных в базе данных.
 * Представляет учетную запись пользователя с именем, паролем, ролью и идентификатором чата.
 */
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /** Уникальный идентификатор пользователя. Генерируется автоматически. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя пользователя. Должно быть уникальным и не может быть пустым. */
    @Column(nullable = false, unique = true)
    private String name;

    /** Хэшированный пароль пользователя. Не может быть пустым. */
    @Column(nullable = false)
    private String password;

    /** Роль пользователя, по умолчанию назначается USER. */
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    /** Идентификатор чата пользователя в Telegram. Может быть null. */
    @Column(name = "chat_id")
    private String chatId;
}

