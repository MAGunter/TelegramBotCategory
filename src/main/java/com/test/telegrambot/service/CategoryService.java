package com.test.telegrambot.service;

import org.springframework.stereotype.Service;

/**
 * Интерфейс для работы с категориями
 */
@Service
public interface CategoryService {
    String addCategory(String name, String parent);
    String addCategory(String name);
    String viewTree();
    String removeCategory(String name);
}
