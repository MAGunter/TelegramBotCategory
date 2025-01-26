package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.service.CategoryService;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ViewTreeCommand implements Command {

    private final CategoryService categoryService;
    private final MessageSender messageSender;

    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String message = categoryService.viewTree();

       messageSender.sendMsg(chatId, message, sender);
    }
}
