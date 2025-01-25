package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.service.CategoryExcelService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class UploadCommand implements Command {

    private final CategoryExcelService categoryExcelService;
    private static final Logger logger = LoggerFactory.getLogger(UploadCommand.class);

    @Override
    public void execute(Update update, AbsSender sender) {
        if (update.getMessage().hasDocument()) {
            handleFileUpload(update, sender);
        } else {
            sendMsg(update.getMessage().getChatId().toString(), "Прикрепите файл когда пишете команду вместе", sender);
        }
    }

    private void handleFileUpload(Update update, AbsSender sender) {
        String chatId = update.getMessage().getChatId().toString();
        String fileId = update.getMessage().getDocument().getFileId();

        try {
            logger.info("Downloading file with ID: {}", fileId);
            org.telegram.telegrambots.meta.api.objects.File file = downloadFile(fileId, sender);
            logger.info("File downloaded successfully: {}", file.getFilePath());

            String filePath = update.getMessage().getDocument().getFileName();
            logger.info("Saving file to: {}", filePath);
            saveFile(filePath, file, sender);
            logger.info("File saved successfully");

            logger.info("Processing file with CategoryExcelService");
            categoryExcelService.importTreeToExcel(filePath);
            logger.info("File processed successfully");

            sendMsg(chatId, "File uploaded successfully", sender);
        } catch (Exception e) {
            sendMsg(chatId, "An error occurred while processing the file.", sender);
            logger.error("Error during file upload", e);
        }
    }

    private org.telegram.telegrambots.meta.api.objects.File downloadFile(String fileId, AbsSender sender) throws TelegramApiException {
        GetFile getFile = new GetFile(fileId);
        return sender.execute(getFile);
    }

    private void saveFile(String filePath, org.telegram.telegrambots.meta.api.objects.File file, AbsSender sender) throws Exception {
        String fileUrl = file.getFileUrl(((TelegramLongPollingBot) sender).getBotToken());
        try (InputStream in = new URL(fileUrl).openStream();
             FileOutputStream out = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    private void sendMsg(String chatId, String text, AbsSender sender) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        try {
            sender.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}




