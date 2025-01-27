package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final MessageSender messageSender;

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
                Важно! При загрузке категорий через excel, файл должен содержать только уникальные значения (не поддерживает дупликаты),
                а так же дерево категорий должно быть пустым,
                в противном случае возможно нарушение структуры дерева категорий.
                """;

        messageSender.sendMsg(chatId, message, sender);
    }
}
