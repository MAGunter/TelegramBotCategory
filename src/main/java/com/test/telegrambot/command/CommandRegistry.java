package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.AddElementCommand;
import com.test.telegrambot.command.impl.HelpCommand;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandRegistry {

    Map<String, Command> commands = new HashMap<>();

    public CommandRegistry(){
        commands.put("/help", new HelpCommand());
        commands.put("/addElement", new AddElementCommand());
    }

    public Command getCommand(String command){
        return commands.get(command);
    }
}
