package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.service.CategoryService;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Класс для реализации команды отображения дерева категорий.
 * <p>
 * Этот класс обрабатывает команду просмотра дерева категорий.
 * При выполнении команды, дерево категорий извлекается из системы и отправляется пользователю в виде текстового сообщения.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ViewTreeCommand implements Command {

    private final CategoryService categoryService;
    private final MessageSender messageSender;

    /**
     * Выполняет команду отображения дерева категорий.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
    @Override
    public void execute(Update update, AbsSender sender) {
        String chatId = update.getMessage().getChatId().toString();
        String message = categoryService.viewTree();

        messageSender.sendMsg(chatId, message, sender);
    }
}

