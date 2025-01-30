package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Класс для реализации команды отображения списка доступных команд.
 * <p>
 * Этот класс обрабатывает команду помощи и отправляет пользователю список всех доступных команд бота.
 * Команды включают действия с категориями, такие как просмотр дерева, добавление, удаление элементов, а также экспорт и импорт данных в формате Excel.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final MessageSender messageSender;

    /**
     * Выполняет команду помощи, отображая список доступных команд.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String message = """
                Доступные команды:
                
                /viewTree - Показать дерево категорий
                /addElement <название категорий> - Добавить корневой элемент
                /addElement <старшая категория> <категория> - Добавить дочернюю категорию
                /removeElement <название категорий> - Удалить элемент
                /help - Показать список команд
                /download - Доступ к дереву категорий через excel | формат в виде .xlsx -> categories.xlsx
                /upload - Загрузить дерево категорий через excel | формат в виде .xlsx
                
                Важно! При загрузке категорий через excel, файл должен содержать только уникальные значения (не поддерживает дубликаты),
                а так же дерево категорий должно быть пустым,
                в противном случае возможно нарушение структуры дерева категорий.
                """;

        messageSender.sendMsg(chatId, message, sender);
    }
}
