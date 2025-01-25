package com.test.telegrambot.utility;

import com.test.telegrambot.command.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class UploadHandler {

    static void sendMsg(String chatId, String text, AbsSender sender){
        SendMessage send = new SendMessage(chatId, text);
        try{
            sender.execute(send);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
