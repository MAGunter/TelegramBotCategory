package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Команда /start для приветствия пользователя и предоставления списка доступных команд.
 * Эта команда используется при первом взаимодействии с ботом.
 */
@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final MessageSender messageSender;

    /**
     * Выполняет команду /start, отправляя пользователю приветственное сообщение
     * с описанием доступных команд.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
    @Override
    public void execute(Update update, AbsSender sender) {
        String chatId = update.getMessage().getChatId().toString();
        String message = """
                Привет! Я бот, который поможет тебе работать с категориями.
                
                Ты на данный момент находишься в роли пользователя где тебе доступны только просмотр категорий.
                а именно:
                
                /viewTree - Показать дерево категорий
                /download - Доступ к дереву категорий через excel | формат в виде .xlsx -> categories.xlsx
                /help - Показать список команд
                /login <логин> <пароль> - Стать админом (получить полный доступ к управлению категориями)
                """;

        messageSender.sendMsg(chatId, message, sender);
    }
}
