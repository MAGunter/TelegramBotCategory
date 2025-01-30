package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.entity.Role;
import com.test.telegrambot.repository.UserRepository;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Команда для входа в систему через Telegram-бота.
 * Позволяет пользователям аутентифицироваться, вводя логин и пароль.
 */
@Component
@RequiredArgsConstructor
public class LoginCommand implements Command {

    private final MessageSender messageSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Выполняет команду входа в систему.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
    @Override
    public void execute(Update update, AbsSender sender) {
        String chatId = update.getMessage().getChatId().toString();
        String[] parameters = update.getMessage().getText().replace("/login", "")
                .trim().split(" ");
        String defaultMessage = "Пример: /login <логин> <пароль>";

        if (parameters.length != 2) {
            messageSender.sendMsg(chatId, defaultMessage, sender);
            return;
        }

        String username = parameters[0];
        String password = parameters[1];

        userRepository.findByName(username).ifPresentOrElse(user -> {
            if (passwordEncoder.matches(password, user.getPassword())) {
                if (username.equals("admin")) {
                    user.setRole(Role.ADMIN);
                    user.setChatId(chatId);
                    userRepository.save(user);
                    messageSender.sendMsg(chatId, "Вы вошли как Администратор", sender);
                } else {
                    messageSender.sendMsg(chatId, "Вы вошли как Пользователь", sender);
                }
            } else {
                messageSender.sendMsg(chatId, "Неверный пароль", sender);
            }
        }, () -> messageSender.sendMsg(chatId, "Пользователь не найден", sender));
    }
}
