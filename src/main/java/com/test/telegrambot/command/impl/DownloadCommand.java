package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.service.CategoryExcelService;
import com.test.telegrambot.utility.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.ByteArrayInputStream;

@Component
@RequiredArgsConstructor
public class DownloadCommand implements Command {

    private final CategoryExcelService categoryExcelService;
    private final MessageSender messageSender;

    @Override
    public void execute(Update update, AbsSender sender) {
        byte[] excelData = categoryExcelService.exportTreeToExcel();
        if(excelData.length == 0){
            messageSender.sendMsg(update.getMessage().getChatId().toString(), "Категории не найдены", sender);
        }
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(update.getMessage().getChatId().toString());
        sendDocument.setDocument(new InputFile(new ByteArrayInputStream(excelData), "categories.xlsx"));

        messageSender.sendMsg(sendDocument, sender);
    }
}
