package com.test.telegrambot.service;

import com.test.telegrambot.entity.Category;
import com.test.telegrambot.repository.CategoryRepository;
import com.test.telegrambot.service.impl.CategoryServiceImpl;
import com.test.telegrambot.utils.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl serviceUnderTest;

    @Test
    @DisplayName("Добавление корневой категории")
    public void givenCategoryName_whenAddRootCategory_thenCategoryIsAdded() {
        // given
        Category electronics = DataUtils.getElectronicsTransient();
        BDDMockito.given(categoryRepository.findByName(electronics.getName())).willReturn(Optional.empty());

        // when
        String response = serviceUnderTest.addCategory(electronics.getName());

        // then
        assertThat(response).isEqualTo("Категория " + electronics.getName() + " успешно добавлена как корневая.");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Добавление существующей корневой категории")
    public void givenExistingCategoryName_whenAddRootCategory_thenReturnDuplicateMessage() {
        // given
        Category electronics = DataUtils.getElectronicsTransient();
        BDDMockito.given(categoryRepository.findByName(electronics.getName())).willReturn(Optional.of(electronics));

        // when
        String response = serviceUnderTest.addCategory(electronics.getName());

        // then
        assertThat(response).isEqualTo("Категория с названием '" + electronics.getName() + "' уже существует.");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Добавление дочерней категории")
    public void givenParentAndChildCategory_whenAddCategory_thenChildIsAdded() {
        // given
        Category electronics = DataUtils.getElectronicsTransient();
        Category laptops = DataUtils.getLaptopsTransient();
        BDDMockito.given(categoryRepository.findByName(electronics.getName())).willReturn(Optional.of(electronics));
        BDDMockito.given(categoryRepository.findByName(laptops.getName())).willReturn(Optional.empty());

        // when
        String response = serviceUnderTest.addCategory(electronics.getName(), laptops.getName());

        // then
        assertThat(response).isEqualTo("Категория " + laptops.getName() + " добавлена, его родитель " + electronics.getName());
        verify(categoryRepository, times(1)).save(electronics);
    }

    @Test
    @DisplayName("Добавление дочерней категории при отсутствии родителя")
    public void givenNonExistentParent_whenAddCategory_thenReturnErrorMessage() {
        // given
        String parentName = "NonExistentParent";
        Category laptops = DataUtils.getLaptopsTransient();
        BDDMockito.given(categoryRepository.findByName(parentName)).willReturn(Optional.empty());

        // when
        String response = serviceUnderTest.addCategory(parentName, laptops.getName());

        // then
        assertThat(response).isEqualTo("Родительская категория " + parentName + " не найдена");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("Просмотр дерева категорий")
    public void whenViewTree_thenReturnCategoryTree() {
        // given
        Category electronics = DataUtils.getElectronicsTransient();
        BDDMockito.given(categoryRepository.findAll()).willReturn(List.of(electronics));

        // when
        String tree = serviceUnderTest.viewTree();

        // then
        assertThat(tree).startsWith("Дерево категорий:");
    }

    @Test
    @DisplayName("Просмотр дерева категорий при отсутствии данных")
    public void whenViewTreeWithNoCategories_thenReturnEmptyMessage() {
        // given
        BDDMockito.given(categoryRepository.findAll()).willReturn(List.of());

        // when
        String tree = serviceUnderTest.viewTree();

        // then
        assertThat(tree).isEqualTo("Дерево категорий пусто.");
    }

    @Test
    @DisplayName("Удаление категории")
    public void givenCategoryName_whenRemoveCategory_thenCategoryIsRemoved() {
        // given
        Category laptops = DataUtils.getLaptopsTransient();
        BDDMockito.given(categoryRepository.findByName(laptops.getName())).willReturn(Optional.of(laptops));

        // when
        String response = serviceUnderTest.removeCategory(laptops.getName());

        // then
        assertThat(response).isEqualTo("Категория " + laptops.getName() + " успешно удалена");
        verify(categoryRepository, times(1)).delete(laptops);
    }

    @Test
    @DisplayName("Удаление категории при её отсутствии")
    public void givenNonExistentCategory_whenRemoveCategory_thenReturnErrorMessage() {
        // given
        String categoryName = "NonExistentCategory";
        BDDMockito.given(categoryRepository.findByName(categoryName)).willReturn(Optional.empty());

        // when
        String response = serviceUnderTest.removeCategory(categoryName);

        // then
        assertThat(response).isEqualTo("Категория " + categoryName + " не найдена");
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}
