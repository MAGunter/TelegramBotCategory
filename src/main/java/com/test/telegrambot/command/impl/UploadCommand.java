package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.utility.MessageSender;
import com.test.telegrambot.utility.UploadHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class UploadCommand implements Command {

    private final UploadHandler uploadHandler;
    private final MessageSender messageSender;

    @Override
    public void execute(Update update, AbsSender sender) {
        if (update.getMessage().hasDocument()) {
            uploadHandler.handleFileUpload(update, sender);
        } else {
            messageSender.sendMsg(update.getMessage().getChatId().toString(), "Прикрепите файл когда пишете команду вместе", sender);
        }
    }
}




