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
public class AddElementCommand implements Command {

    private final CategoryService categoryService;

    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        String parameters = message.replace("/addElement ", "").trim();
        String defaultMessage = "Вы неверно ввели команду или название категорий. " +
                "Пример: /addElement <категория> или addElement <родитель категорий> <категория>";

        String response;
        if(parameters.isEmpty() || message.equals("/addElement")){
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

        SendMessage send = new SendMessage(chatId, response);

        try{
            sender.execute(send);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
