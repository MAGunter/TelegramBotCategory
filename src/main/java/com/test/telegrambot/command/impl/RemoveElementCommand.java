package com.test.telegrambot.command.impl;

import com.test.telegrambot.command.Command;
import com.test.telegrambot.entity.Role;
import com.test.telegrambot.service.CategoryService;
import com.test.telegrambot.utility.MessageSender;
import com.test.telegrambot.utility.SecurityCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * Класс для реализации команды удаления элемента из дерева категорий.
 * <p>
 * Этот класс обрабатывает команду удаления категории из системы.
 * Для удаления необходимо указать название категории, которую нужно удалить.
 * Если введены некорректные параметры, пользователю будет отправлено сообщение с ошибкой.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class RemoveElementCommand implements Command {

    private final CategoryService categoryService;
    private final MessageSender messageSender;
    private final SecurityCheck securityCheck;

    /**
     * Выполняет команду удаления элемента (категории) из дерева категорий.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
    @Override
    public void execute(Update update, AbsSender sender){
        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        String parameters = message.replace("/removeElement ", "").trim();
        String defaultMessage = "Вы неверно ввели команду или название категории. " +
                "Пример: /removeElement <категория>";

        if(!securityCheck.hasRole(Role.ADMIN, update)){
            messageSender.sendMsg(update.getMessage().getChatId().toString(),
                    "У вас нет прав для выполнения данной команды", sender);
            return;
        }

        String response;
        if(parameters.isEmpty() || message.equals("/removeElement")){
            response = defaultMessage;
        }else{
            response = categoryService.removeCategory(parameters);
        }

        messageSender.sendMsg(chatId, response, sender);
    }
}
