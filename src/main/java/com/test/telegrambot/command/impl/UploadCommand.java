package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.entity.Role;
import com.test.telegrambot.utility.MessageSender;
import com.test.telegrambot.utility.SecurityCheck;
import com.test.telegrambot.utility.UploadHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Класс для реализации команды загрузки файла с деревом категорий.
 * <p>
 * Этот класс обрабатывает команду загрузки файла, прикрепленного к сообщению.
 * Если файл присутствует в сообщении, он дальше обрабатывается в загрузки.
 * Если файл не прикреплен, пользователю отправляется сообщение с просьбой прикрепить файл.
 * Важно! При загрузке категорий, дерево категорий не должен содержать дубликатов или должно быть пустым.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class UploadCommand implements Command {

    private final UploadHandler uploadHandler;
    private final MessageSender messageSender;
    private final SecurityCheck securityCheck;

    /**
     * Выполняет команду загрузки файла с деревом категорий.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
    @Override
    public void execute(Update update, AbsSender sender) {

        if(!securityCheck.hasRole(Role.ADMIN, update)){
            messageSender.sendMsg(update.getMessage().getChatId().toString(),
                    "У вас нет прав для выполнения данной команды", sender);
            return;
        }

        if (update.getMessage().hasDocument()) {
            uploadHandler.handleFileUpload(update, sender);
        } else {
            messageSender.sendMsg(update.getMessage().getChatId().toString(), "Прикрепите файл когда пишете команду вместе", sender);
        }
    }
}





