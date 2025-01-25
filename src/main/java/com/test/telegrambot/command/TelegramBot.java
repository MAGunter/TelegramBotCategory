package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.UploadCommand;
import com.test.telegrambot.config.BotConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CommandRegistry commandRegistry;
    private final UploadCommand uploadCommand;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken(){
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update){
        if(update.hasMessage() && update.getMessage().hasText()){
            String command = update.getMessage().getText().split(" ")[0];
            Command cmd = commandRegistry.getCommand(command);

            if(cmd != null){
                cmd.execute(update, this);
            }else{
                SendMsg(update.getMessage().getChatId().toString());
            }
        }else if(update.hasMessage() && update.getMessage().hasDocument()){
            uploadCommand.execute(update, this);
        }
    }

    private void SendMsg(String chatId){
        SendMessage send = new SendMessage(chatId, "Неизвестная команда, напишите /help для помощи");
        try{
            execute(send);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
