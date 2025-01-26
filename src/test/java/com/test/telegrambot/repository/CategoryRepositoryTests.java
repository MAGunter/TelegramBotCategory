package com.test.telegrambot.repository;

import com.test.telegrambot.entity.Category;
import com.test.telegrambot.utils.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("Создание категории: при сохранении категории создается новая запись")
    public void givenCategoryShop_whenSave_thenCreateCategory() {
        // given
        Category category = DataUtils.getShopTransient();
        // when
        Category savedCategory = categoryRepository.save(category);
        // then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isNotNull();
    }

    @Test
    @DisplayName("Сохранение связи: родительская категория сохраняется у дочерней")
    public void givenCategoryWithParent_whenSave_thenPersistParentChildRelationship() {
        // given
        Category parentCategory = categoryRepository.save(DataUtils.getShopTransient());
        Category childCategory = DataUtils.getAppleTransient();
        childCategory.setParent(parentCategory);

        // when
        Category savedChild = categoryRepository.save(childCategory);

        // then
        assertThat(savedChild).isNotNull();
        assertThat(savedChild.getParent()).isNotNull();
        assertThat(savedChild.getParent().getId()).isEqualTo(parentCategory.getId());
    }

    @Test
    @DisplayName("Сохранение категории с дочерними: при сохранении родителя сохраняются дочерние категории")
    public void givenParentCategoryWithChildren_whenSave_thenPersistAllChildren() {
        // given
        Category parentCategory = DataUtils.getShopTransient();
        Category childCategory1 = DataUtils.getAppleTransient();
        Category childCategory2 = Category.builder().name("Electronics").build();

        parentCategory.getChildren().add(childCategory1);
        parentCategory.getChildren().add(childCategory2);
        childCategory1.setParent(parentCategory);
        childCategory2.setParent(parentCategory);

        // when
        Category savedParent = categoryRepository.save(parentCategory);

        // then
        assertThat(savedParent).isNotNull();
        assertThat(savedParent.getChildren()).hasSize(2);
        assertThat(savedParent.getChildren())
                .extracting("name")
                .containsExactlyInAnyOrder("Apple", "Electronics");
    }

    @Test
    @DisplayName("Поиск категории: возвращается категория по имени")
    public void givenExistingCategory_whenFindByName_thenReturnCategory() {
        // given
        Category category = categoryRepository.save(DataUtils.getShopTransient());

        // when
        Category foundCategory = categoryRepository.findByName(category.getName()).orElse(null);

        // then
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo(category.getName());
    }

    @Test
    @DisplayName("Удаление родителя: при удалении родительской категории удаляются дочерние")
    public void givenCategoryWithChildren_whenDelete_thenDeleteAllChildren() {
        // given
        Category parentCategory = categoryRepository.save(DataUtils.getShopTransient());
        Category childCategory = DataUtils.getAppleTransient();
        childCategory.setParent(parentCategory);
        parentCategory.getChildren().add(childCategory);

        categoryRepository.save(parentCategory);

        // when
        categoryRepository.delete(parentCategory);

        // then
        List<Category> allCategories = categoryRepository.findAll();
        assertThat(allCategories).isEmpty();
    }

    @Test
    @DisplayName("Удаление категории: категория без дочерних удаляется без ошибок")
    public void givenCategoryWithoutChildren_whenDelete_thenNoErrorsOccur() {
        // given
        Category category = categoryRepository.save(DataUtils.getShopTransient());

        // when
        categoryRepository.delete(category);

        // then
        List<Category> allCategories = categoryRepository.findAll();
        assertThat(allCategories).isEmpty();
    }
}


