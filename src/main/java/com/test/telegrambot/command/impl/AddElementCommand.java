package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
public class AddElementCommand implements Command {

    private final CategoryService categoryService;

    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String messageText = update.getMessage().getText();
        String parameters = messageText.replace("/addElement ", "").trim();
        String defaultMessage = "Вы неверно ввели команду или название категорий. " +
                "Пример: /addElement <категория> или addElement <категория> <родитель категорий>";

        String response;
        if(parameters.isEmpty()){
            response = defaultMessage;
        }else{
            String[] args = parameters.split(" ");
            if(args.length == 1){
                response = categoryService.addCategory(args[0]);
            }else if(args.length == 2){
                response = categoryService.addCategory(args[0], args[1]);
            }else{
                response = defaultMessage;
            }
        }

        SendMessage message = new SendMessage(chatId, response);

        try{
            sender.execute(message);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
