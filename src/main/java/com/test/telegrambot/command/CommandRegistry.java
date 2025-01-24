package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandRegistry {

    Map<String, Command> commands = new HashMap<>();

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
            }
        }
    }

    public Command getCommand(String command){
        return commands.get(command);
    }
}
