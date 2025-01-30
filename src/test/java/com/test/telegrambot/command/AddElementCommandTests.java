package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.AddElementCommand;
import com.test.telegrambot.entity.Role;
import com.test.telegrambot.utility.MessageSender;
import com.test.telegrambot.utility.SecurityCheck;
import com.test.telegrambot.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddElementCommandTests {

    @Mock
    private CategoryService categoryService;

    @Mock
    private MessageSender messageSender;

    @Mock
    private SecurityCheck securityCheck;

    @InjectMocks
    private AddElementCommand addElementCommand;

    @Test
    @DisplayName("Add element with admin role")
    public void givenAdminRole_whenAddElement_thenElementAdded() {
        // given
        String chatId = "12345";
        String categoryName = "Electronics";
        Update update = createUpdateWithMessage("/addElement " + categoryName);
        doReturn(true).when(securityCheck).hasRole(eq(Role.ADMIN), eq(update));
        doReturn("Категория " + categoryName + " успешно добавлена").when(categoryService).addCategory(categoryName);

        // when
        AbsSender sender = mock(AbsSender.class);
        addElementCommand.execute(update, sender);

        // then
        verify(messageSender, times(1)).sendMsg(chatId, "Категория " + categoryName + " успешно добавлена", sender);
    }

    @Test
    @DisplayName("Add element with invalid command")
    public void givenInvalidCommand_whenAddElement_thenErrorMessage() {
        // given
        String chatId = "12345";
        String invalidCommand = "/addElement";
        Update update = createUpdateWithMessage(invalidCommand);
        doReturn(true).when(securityCheck).hasRole(eq(Role.ADMIN), eq(update));  // Ensure the arguments match

        // when
        AbsSender sender = mock(AbsSender.class);
        addElementCommand.execute(update, sender);

        // then
        verify(messageSender, times(1))
                .sendMsg(chatId, "Вы неверно ввели команду или название категорий. Пример: /addElement <категория> или addElement <родитель категорий> <категория>", sender);
    }

    private Update createUpdateWithMessage(String messageText) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn(messageText);
        when(message.getChatId()).thenReturn(12345L);
        return update;
    }
}
