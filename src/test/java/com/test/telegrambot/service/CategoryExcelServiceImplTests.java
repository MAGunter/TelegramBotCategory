package com.test.telegrambot.service;

import com.test.telegrambot.entity.Category;
import com.test.telegrambot.repository.CategoryRepository;
import com.test.telegrambot.service.impl.CategoryExcelServiceImpl;
import com.test.telegrambot.utility.DataUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryExcelServiceImplTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryExcelServiceImpl categoryExcelService;

    @Test
    @DisplayName("Тест экспорта дерева категорий в Excel")
    public void givenCategories_whenExportTreeToExcel_thenByteArrayIsReturned() {
        // given
        Category category1 = DataUtils.getElectronicsTransient();
        Category category2 = DataUtils.getSmartphonesTransient();
        BDDMockito.given(categoryRepository.findAll()).willReturn(List.of(category1, category2));

        // when
        byte[] excelBytes = categoryExcelService.exportTreeToExcel();

        // then
        assertThat(excelBytes).isNotEmpty();
    }

    @Test
    @DisplayName("Тест экспорта дерева категорий в Excel, если категории отсутствуют")
    public void givenNoCategories_whenExportTreeToExcel_thenEmptyByteArrayIsReturned() {
        // given
        BDDMockito.given(categoryRepository.findAll()).willReturn(List.of());

        // when
        byte[] excelBytes = categoryExcelService.exportTreeToExcel();

        // then
        assertThat(excelBytes).isEmpty();
    }

    @Test
    @DisplayName("Тест импорта дерева категорий с неожиданным типом ячейки")
    public void givenExcelFileWithUnexpectedCellType_whenImportTreeToExcel_thenExceptionIsThrown() throws Exception {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Дерево Категорий");

            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Category1");
            row.createCell(1).setCellValue(12345); // Некорректный тип данных

            workbook.write(outputStream);
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // when & then
        assertThrows(RuntimeException.class, () -> categoryExcelService.importTreeToExcel(inputStream.toString()));
        verify(categoryRepository, never()).save(any(Category.class));
    }
}
