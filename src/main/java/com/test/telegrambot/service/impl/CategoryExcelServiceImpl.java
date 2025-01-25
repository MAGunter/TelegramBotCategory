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
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryExcelServiceImpl implements CategoryExcelService {
    private final CategoryRepository categoryRepository;

    @Override
    public byte[] exportTreeToExcel() {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Дерево Категорий");
            List<Category> categories = categoryRepository.findAll();
            int rowNum = 0;

            if(categories.isEmpty()){
                System.out.println("Не смогли найти категории");
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
            throw new RuntimeException(e);
        }
    }


    @Override
    public void importTreeToExcel(String excelFile) {
        try (FileInputStream inputStream = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                String categoryName = currentRow.getCell(0).getStringCellValue();
                String parentCategoryName = null;

                if (currentRow.getCell(1) != null) {
                    switch (currentRow.getCell(1).getCellType()) {
                        case STRING:
                            parentCategoryName = currentRow.getCell(1).getStringCellValue();
                            break;
                        case FORMULA:
                            parentCategoryName = currentRow.getCell(1).getCellFormula();
                            break;
                        default:
                            throw new IllegalStateException("Неожиданный тип ячейки: " + currentRow.getCell(1).getCellType());
                    }
                }

                Category category = new Category();
                category.setName(categoryName);

                if (parentCategoryName != null) {
                    Category parentCategory = categoryRepository.findByName(parentCategoryName)
                            .orElse(null);
                    category.setParent(parentCategory);
                }

                categoryRepository.save(category);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
