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
public class RemoveElementCommand implements Command {

    private final CategoryService categoryService;

    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        String parameters = message.replace("/removeElement ", "").trim();
        String defaultMessage = "Вы неверно ввели команду или название категории. " +
                "Пример: /removeElement <категория>";

        String response;
        if(parameters.isEmpty() || message.equals("/removeElement")){
            response = defaultMessage;
        }else{
            response = categoryService.removeCategory(parameters);
        }

        SendMessage send = new SendMessage(chatId, response);
        try{
            sender.execute(send);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
