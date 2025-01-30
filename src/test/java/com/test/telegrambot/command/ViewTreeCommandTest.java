package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.ViewTreeCommand;
import com.test.telegrambot.service.CategoryService;
import com.test.telegrambot.utility.MessageSender;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ViewTreeCommandTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private MessageSender messageSender;

    @Mock
    private Update update;

    @Mock
    private Message message;

    @InjectMocks
    private ViewTreeCommand viewTreeCommand;

    @Test
    @DisplayName("Успешное получение дерева категорий")
    public void execute_whenTreeExists_thenSendTreeMessage() {
        // given
        given(update.getMessage()).willReturn(message);
        given(message.getChatId()).willReturn(1L);
        given(categoryService.viewTree()).willReturn("Дерево категорий:\n-> Категория 1");

        // when
        viewTreeCommand.execute(update, null);

        // then
        verify(messageSender, times(1)).sendMsg("1", "Дерево категорий:\n-> Категория 1", null);
    }

    @Test
    @DisplayName("Дерево категорий пустое")
    public void execute_whenTreeIsEmpty_thenSendEmptyMessage() {
        // given
        given(update.getMessage()).willReturn(message);
        given(message.getChatId()).willReturn(1L);
        given(categoryService.viewTree()).willReturn("Дерево категорий пусто.");

        // when
        viewTreeCommand.execute(update, null);

        // then
        verify(messageSender, times(1)).sendMsg("1", "Дерево категорий пусто.", null);
    }

    @Test
    @DisplayName("Обновление не содержит сообщение")
    public void execute_whenUpdateHasNoMessage_thenDoNothing() {
        // given
        given(update.getMessage()).willReturn(null);

        // when
        viewTreeCommand.execute(update, null);

        // then
        verifyNoInteractions(messageSender, categoryService);
    }
}

