package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public class HelpCommand implements Command {
    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String message = """
                Доступные команды:
                /viewTree - Показать дерево категорий
                /addElement <название> - Добавить корневой элемент
                /addElement <родитель> <дочерний> - Добавить дочерний элемент
                /removeElement <название> - Удалить элемент
                /help - Показать список команд
                """;

        SendMessage send = new SendMessage(chatId, message);
        try{
            sender.execute(send);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
