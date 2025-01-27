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

/**
 * Класс для реализации команды скачивания данных о категориях в формате Excel.
 * <p>
 * Этот класс обрабатывает команду скачивания дерева категорий и отправляет файл Excel пользователю.
 * Файл содержит все категории в системе | categories.xlsx.
 * Если категорий нет, пользователю отправляется сообщение об ошибке.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class DownloadCommand implements Command {

    private final CategoryExcelService categoryExcelService;
    private final MessageSender messageSender;

    /**
     * Выполняет команду скачивания дерева категорий в формате Excel.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
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
