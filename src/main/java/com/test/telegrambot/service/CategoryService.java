package com.test.telegrambot.service;

import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    String addCategory(String name, String parent);
    String addCategory(String name);
}
