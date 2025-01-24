package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.service.CategoryExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
public class DownloadCommand implements Command {
    private final CategoryExcelService categoryExcelService;

    @Override
    public void execute(Update update, AbsSender sender) {
        byte[] excelData = categoryExcelService.exportTreeToExcel();
        if(excelData.length == 0){
            SendMessage emptyMsg = new SendMessage(update.getMessage().getChatId().toString(), "Категорий не найдены");
            try{
                sender.execute(emptyMsg);
            }catch(TelegramApiException e){
                e.printStackTrace();
            }
        }
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(update.getMessage().getChatId().toString());
        sendDocument.setDocument(new InputFile(new ByteArrayInputStream(excelData), "categories.xlsx"));

        try{
            sender.execute(sendDocument);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
}
