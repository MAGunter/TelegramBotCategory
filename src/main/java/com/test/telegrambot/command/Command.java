package com.test.telegrambot.command;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface Command {
    void execute(Update update, AbsSender sender);
}
