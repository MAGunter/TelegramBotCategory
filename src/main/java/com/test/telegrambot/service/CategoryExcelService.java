package com.test.telegrambot.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface CategoryExcelService {
    byte[] exportTreeToExcel();
    void importTreeToExcel(String excelFile);
}
