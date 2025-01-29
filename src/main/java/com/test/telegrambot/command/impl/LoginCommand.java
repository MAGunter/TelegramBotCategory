package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class RegistrationCommand implements Command {

    private final MessageSender messageSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void execute(Update update, AbsSender sender) {
        String chatId = update.getMessage().getChatId().toString();
        String[] parameters = update.getMessage().getText().replace("/registration", "")
                .trim().split(" ");
        String defaultMessage = "Вы неверно ввели команду или название категорий. " +
                "Пример: /registration <логин> <пароль>";

        if(parameters.length != 2){
            messageSender.sendMsg(chatId, defaultMessage, sender);
            return;
        }

        String username = parameters[0];
        String password = parameters[1];

        if(username.isEmpty() || password.isEmpty()){
            messageSender.sendMsg(chatId, defaultMessage, sender);
            return;
        }

        if(username.equals("admin") && password.equals(passwordEncoder.encode(password))){
            messageSender.sendMsg(chatId, "Смена статуса на Администратора", sender);
        }

        messageSender.sendMsg(chatId, defaultMessage, sender);
    }
}
