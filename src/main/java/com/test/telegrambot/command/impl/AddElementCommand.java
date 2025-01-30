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
 * Класс для реализации команды добавления элемента в категорию.
 * <p>
 * Этот класс обрабатывает команду добавления нового элемента (категории) в систему.
 * Он может работать с двумя вариантами ввода:
 * 1. Один параметр — добавление категории.
 * 2. Два параметра — добавление подкатегории в указанную родительскую категорию.
 * <p>
 * Команда имеет формат:
 * <ul>
 * <li>/addElement <категория> — добавляет категорию;</li>
 * <li>/addElement <старшая категория> <категория> — добавляет подкатегорию в указанную родительскую категорию;</li>
 * </ul>
 * Если введены некорректные параметры, выводится сообщение об ошибке.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class AddElementCommand implements Command {

    private final CategoryService categoryService;
    private final MessageSender messageSender;
    private final SecurityCheck securityCheck;

    /**
     * Выполняет команду добавления категории или подкатегории.
     *
     * @param update Обновление, содержащее информацию о сообщении.
     * @param sender Объект, использующийся для отправки сообщений в чат.
     */
    @Override
    public void execute(Update update, AbsSender sender){

        if(!securityCheck.hasRole(Role.ADMIN, update)){
            messageSender.sendMsg(update.getMessage().getChatId().toString(),
                    "У вас нет прав для выполнения данной команды", sender);
            return;
        }

        String chatId = update.getMessage().getChatId().toString();
        String message = update.getMessage().getText();
        String parameters = message.replace("/addElement ", "").trim();
        String defaultMessage = "Вы неверно ввели команду или название категорий. " +
                "Пример: /addElement <категория> или addElement <родитель категорий> <категория>";

        String response;
        if(parameters.isEmpty() || message.equals("/addElement")){
            response = defaultMessage;
        }else{
            String[] args = parameters.split(" ");
            if(args.length == 1){
                response = categoryService.addCategory(args[0]);
            }else if(args.length == 2){
                response = categoryService.addCategory(args[0], args[1]);
            }else{
                response = defaultMessage;
            }
        }
        messageSender.sendMsg(chatId, response, sender);
    }
}
