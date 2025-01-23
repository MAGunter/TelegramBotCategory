package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ViewTreeCommand implements Command {
    private final CategoryService categoryService;

    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String message = categoryService.viewTree();

        SendMessage send = new SendMessage(chatId, message);
        try{
            sender.execute(send);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
