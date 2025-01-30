package com.test.telegrambot.utility;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateMessage {
    public static Update createUpdateWithMessage(String messageText) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);

        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn(messageText);

        return update;
    }
}

