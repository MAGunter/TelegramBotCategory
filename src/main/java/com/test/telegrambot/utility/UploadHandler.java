package com.test.telegrambot.utility;

import com.test.telegrambot.service.CategoryExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class UploadHandler {

    private final CategoryExcelService categoryExcelService;
    private final MessageSender messageSender;

    public void handleFileUpload(Update update, AbsSender sender) {
        String chatId = update.getMessage().getChatId().toString();
        String fileId = update.getMessage().getDocument().getFileId();

        try {
            File file = downloadFile(fileId, sender);

            String filePath = update.getMessage().getDocument().getFileName();
            saveFile(filePath, file, sender);

            categoryExcelService.importTreeToExcel(filePath);

            messageSender.sendMsg(chatId, "Файл загружен успешно, результат можете увидеть в базе данных." + "\n" +
                    "Или вместо этого введите команду /viewTree", sender);
        } catch (Exception e) {
            messageSender.sendMsg(chatId, "Ошибка при обработке файла, убедитесь что ввели команду корректно" + "\n" +
                    "или что дерево категорий пусто", sender);
        }
    }

    private File downloadFile(String fileId, AbsSender sender) throws TelegramApiException {
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
}
