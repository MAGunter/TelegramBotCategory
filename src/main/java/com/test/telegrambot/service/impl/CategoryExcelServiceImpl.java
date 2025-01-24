package com.test.telegrambot.service.impl;

import com.test.telegrambot.entity.Category;
import com.test.telegrambot.repository.CategoryRepository;
import com.test.telegrambot.service.CategoryExcelService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryExcelServiceImpl implements CategoryExcelService {
    private final CategoryRepository categoryRepository;

    @Override
    public byte[] exportTreeToExcel() {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Category Tree");
            List<Category> categories = categoryRepository.findAll();
            int rowNum = 0;

            if(categories.isEmpty()){
                System.out.println("No categories found");
                return new byte[0];
            }

            for (Category category : categories) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(category.getName());
                if (category.getParent() != null) {
                    row.createCell(1).setCellValue(category.getParent().getName());
                }
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }


    @Override
    public void importTreeToExcel(InputStream input) {
        
    }
}
