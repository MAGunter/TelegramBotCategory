package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для регистрации команд
 */
@Component
public class CommandRegistry {

    Map<String, Command> commands = new HashMap<>();

    /**
     * Конструктор, который регистрирует команды
     * @param commandList список команд который мы получаем
     */
    public CommandRegistry(List<Command> commandList) {
        for (Command command : commandList) {
            if (command instanceof AddElementCommand) {
                commands.put("/addElement", command);
            } else if (command instanceof HelpCommand) {
                commands.put("/help", command);
            } else if(command instanceof ViewTreeCommand){
                commands.put("/viewTree", command);
            } else if(command instanceof RemoveElementCommand){
                commands.put("/removeElement", command);
            } else if(command instanceof DownloadCommand){
                commands.put("/download", command);
            } else if(command instanceof UploadCommand){
                commands.put("/upload", command);
            } else if(command instanceof LoginCommand){
                commands.put("/login", command);
            } else if(command instanceof StartCommand){
                commands.put("/start", command);
            }
        }
    }

    public Command getCommand(String command){
        return commands.get(command);
    }
}
