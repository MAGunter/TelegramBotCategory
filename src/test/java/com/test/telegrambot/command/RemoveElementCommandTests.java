package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.RemoveElementCommand;
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
public class RemoveElementCommandTests {

    @Mock
    private CategoryService categoryService;

    @Mock
    private MessageSender messageSender;

    @Mock
    private SecurityCheck securityCheck;

    @InjectMocks
    private RemoveElementCommand removeElementCommand;

    @Test
    @DisplayName("Remove element with admin role")
    public void givenAdminRole_whenRemoveElement_thenElementRemoved() {
        // given
        String chatId = "12345";
        String categoryName = "Electronics";
        Update update = createUpdateWithMessage("/removeElement " + categoryName);
        doReturn(true).when(securityCheck).hasRole(eq(Role.ADMIN), eq(update));
        doReturn("Категория " + categoryName + " успешно удалена").when(categoryService).removeCategory(categoryName);

        // when
        AbsSender sender = mock(AbsSender.class);
        removeElementCommand.execute(update, sender);

        // then
        verify(messageSender, times(1)).sendMsg(chatId, "Категория " + categoryName + " успешно удалена", sender);
    }

    @Test
    @DisplayName("Remove element without admin role")
    public void givenNoAdminRole_whenRemoveElement_thenAccessDenied() {
        // given
        String chatId = "12345";
        String categoryName = "Electronics";
        Update update = createUpdateWithMessage("/removeElement " + categoryName);
        doReturn(false).when(securityCheck).hasRole(eq(Role.ADMIN), eq(update));

        // when
        AbsSender sender = mock(AbsSender.class);
        removeElementCommand.execute(update, sender);

        // then
        verify(messageSender, times(1)).sendMsg(chatId, "У вас нет прав для выполнения данной команды", sender);
    }

    @Test
    @DisplayName("Remove element with invalid command")
    public void givenInvalidCommand_whenRemoveElement_thenErrorMessage() {
        // given
        String chatId = "12345";
        String invalidCommand = "/removeElement";
        Update update = createUpdateWithMessage(invalidCommand);
        doReturn(true).when(securityCheck).hasRole(eq(Role.ADMIN), eq(update));

        // when
        AbsSender sender = mock(AbsSender.class);
        removeElementCommand.execute(update, sender);

        // then
        verify(messageSender, times(1))
                .sendMsg(chatId, "Вы неверно ввели команду или название категории. Пример: /removeElement <категория>", sender);
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