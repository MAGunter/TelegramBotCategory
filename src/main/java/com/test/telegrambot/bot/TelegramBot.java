package com.test.telegrambot.bot;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.command.CommandRegistry;
import com.test.telegrambot.command.impl.UploadCommand;
import com.test.telegrambot.config.BotConfig;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Классический подход к созданию бота через TelegramLongPollingBot
 */
@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CommandRegistry commandRegistry;
    private final UploadCommand uploadCommand;
    private final MessageSender messageSender;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken(){
        return botConfig.getBotToken();
    }

    /**
     * Обработка входящих сообщений
     * @param update - объект, содержащий информацию о входящем сообщении
     *               (текст, файл, фото, видео и т.д.)
     */
    @Override
    public void onUpdateReceived(Update update){
        if(update.hasMessage() && update.getMessage().hasText()){
            String command = update.getMessage().getText().split(" ")[0];
            Command cmd = commandRegistry.getCommand(command);

            if(cmd != null){
                cmd.execute(update, this);
            }else{
                messageSender.sendMsg(update.getMessage().getChatId().toString(), this);
            }
        }else if(update.hasMessage() && update.getMessage().hasDocument()){
            uploadCommand.execute(update, this);
        }
    }
}
