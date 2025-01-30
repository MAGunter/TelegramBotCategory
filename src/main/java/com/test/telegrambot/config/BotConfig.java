package com.test.telegrambot.config;

import com.test.telegrambot.entity.Role;
import com.test.telegrambot.entity.User;
import com.test.telegrambot.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс конфигурации бота
 * Собирает реквизиты бота из application.properties
 */
@Data
@Configuration
@PropertySource("/application.properties")
@RequiredArgsConstructor
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String botToken;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public String setUpRoles(){
        userRepository.deleteAll();
        return "Admin and User created";
    }

    @Bean
    public String createAdmin(){
        User admin = new User();
        admin.setName("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        return "Admin created";
    }

}
