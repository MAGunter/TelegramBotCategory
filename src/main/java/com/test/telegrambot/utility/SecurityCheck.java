package com.test.telegrambot.utility;

import com.test.telegrambot.entity.Role;
import com.test.telegrambot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Класс для проверки прав доступа пользователей на основе их роли.
 * Используется для авторизации команд в боте.
 */
@Component
@RequiredArgsConstructor
public class SecurityCheck {

    private final UserRepository userRepository;

    /**
     * Проверяет, имеет ли пользователь указанную роль.
     *
     * @param role   роль, которую необходимо проверить
     * @param update обновление с сообщением, содержащим идентификатор чата пользователя
     * @return {@code true}, если пользователь обладает указанной ролью, иначе {@code false}
     */
    public boolean hasRole(Role role, Update update) {
        String chatId = update.getMessage().getChatId().toString();
        return userRepository.findByChatId(chatId)
                .map(user -> user.getRole().equals(role))
                .orElse(false);
    }
}

