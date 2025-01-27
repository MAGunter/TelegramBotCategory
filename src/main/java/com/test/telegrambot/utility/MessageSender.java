package com.test.telegrambot.utility;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Класс для отправки сообщений (помогает сделать код более читаемым)
 */
@Component
public class MessageSender {

    public void sendMsg(String chatId, String text, AbsSender sender){
        SendMessage send = new SendMessage(chatId, text);
        try{
            sender.execute(send);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(String chatId, AbsSender sender){
        SendMessage send = new SendMessage(chatId, "Неизвестная команда, напишите /help для помощи");
        try{
            sender.execute(send);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(SendDocument sendDocument, AbsSender sender){
        try{
            sender.execute(sendDocument);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
