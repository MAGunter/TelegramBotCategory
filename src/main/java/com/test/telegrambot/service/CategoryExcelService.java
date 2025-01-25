package com.test.telegrambot.service;

import org.springframework.stereotype.Service;

@Service
public interface CategoryExcelService {
    byte[] exportTreeToExcel();
    void importTreeToExcel(String excelFile);
}
