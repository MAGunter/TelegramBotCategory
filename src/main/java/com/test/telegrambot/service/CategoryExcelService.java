package com.test.telegrambot.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Интерфейс для работы с excel для манипуляции с категориями
 */
@Service
public interface CategoryExcelService {
    byte[] exportTreeToExcel();
    void importTreeToExcel(String excelFile);
}
