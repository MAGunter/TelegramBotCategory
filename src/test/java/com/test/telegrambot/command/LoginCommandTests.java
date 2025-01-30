package com.test.telegrambot.command;

import com.test.telegrambot.command.impl.LoginCommand;
import com.test.telegrambot.entity.Role;
import com.test.telegrambot.entity.User;
import com.test.telegrambot.repository.UserRepository;
import com.test.telegrambot.utility.MessageSender;
import com.test.telegrambot.utility.UpdateMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginCommandTests {

    @Mock
    private MessageSender messageSender;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginCommand loginCommand;

    @Test
    @DisplayName("Успешный вход как администратор")
    public void givenCorrectCredentials_whenLogin_thenAdminLoggedIn() {
        // given
        String chatId = "12345";
        String username = "admin";
        String password = "password";
        User user = new User();
        user.setChatId(chatId);
        user.setName(username);
        user.setPassword(password);
        user.setRole(Role.USER);
        BDDMockito.given(userRepository.findByName(username)).willReturn(Optional.of(user));
        BDDMockito.given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);

        // when
        Update update = UpdateMessage.createUpdateWithMessage("/login " + username + " " + password);
        AbsSender sender = mock(AbsSender.class);
        loginCommand.execute(update, sender);

        // then
        assertThat(user.getRole()).isEqualTo(Role.ADMIN);
        verify(messageSender, times(1)).sendMsg(chatId, "Вы вошли как Администратор", sender);
    }

    @Test
    @DisplayName("Неверный пароль при входе")
    public void givenIncorrectPassword_whenLogin_thenErrorMessage() {
        // given
        String chatId = "12345";
        String username = "admin";
        String password = "wrongPassword";
        User user = new User();
        user.setChatId(chatId);
        user.setName(username);
        user.setPassword(password);
        user.setRole(Role.USER);
        BDDMockito.given(userRepository.findByName(username)).willReturn(Optional.of(user));
        BDDMockito.given(passwordEncoder.matches(password, user.getPassword())).willReturn(false);

        // when
        Update update = UpdateMessage.createUpdateWithMessage("/login " + username + " " + password);
        AbsSender sender = mock(AbsSender.class);
        loginCommand.execute(update, sender);

        // then
        verify(messageSender, times(1)).sendMsg(chatId, "Неверный пароль", sender);
    }

    @Test
    @DisplayName("Пользователь не найден при входе")
    public void givenNonExistentUser_whenLogin_thenUserNotFoundMessage() {
        // given
        String chatId = "12345";
        String username = "admin";
        String password = "password";
        BDDMockito.given(userRepository.findByName(username)).willReturn(Optional.empty());

        // when
        Update update = UpdateMessage.createUpdateWithMessage("/login " + username + " " + password);
        AbsSender sender = mock(AbsSender.class);
        loginCommand.execute(update, sender);

        // then
        verify(messageSender, times(1)).sendMsg(chatId, "Пользователь не найден", sender);
    }
}
