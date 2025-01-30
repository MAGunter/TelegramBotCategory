package com.test.telegrambot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Конфигурационный класс безопасности, определяющий настройки шифрования паролей.
 * Использует BCrypt для безопасного хранения паролей пользователей.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Создает и настраивает bean для кодирования паролей.
     * BCrypt является надежным алгоритмом хеширования, обеспечивающим защиту паролей.
     *
     * @return экземпляр BCryptPasswordEncoder для кодирования паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
