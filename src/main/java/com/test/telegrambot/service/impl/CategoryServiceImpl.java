package com.test.telegrambot.service.impl;

import com.test.telegrambot.entity.Category;
import com.test.telegrambot.repository.CategoryRepository;
import com.test.telegrambot.service.CategoryService;
import com.test.telegrambot.utility.BuildTree;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления категориями.
 * <p>
 * Этот класс предоставляет методы для добавления, удаления и отображения категорий.
 * Он работает с репозиторием категорий и поддерживает операции с родительскими и дочерними категориями.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Добавляет новую категорию.
     *
     * @param parent Родительская категория (может быть null, если категория корневая).
     * @param child Название новой категории.
     * @return Сообщение о результате операции.
     */
    @Override
    public String addCategory(String parent, String child) {
        if (parent == null) {
            return addCategory(child); // Добавить как корневую категорию
        }

        Optional<Category> parentOpt = categoryRepository.findByName(parent);
        if (parentOpt.isPresent()) {
            Optional<Category> existChild = categoryRepository.findByName(child);
            if (existChild.isPresent()) {
                return "Категория " + child + " уже существует";
            }

            Category parentCategory = parentOpt.get();
            Category childCategory = new Category();
            childCategory.setName(child);
            childCategory.setParent(parentCategory);
            parentCategory.getChildren().add(childCategory);
            categoryRepository.save(parentCategory);

            return "Категория " + child + " добавлена, её родитель " + parent;
        }
        return "Родительская категория " + parent + " не найдена";
    }

    /**
     * Добавляет корневую категорию.
     *
     * @param name Название новой категории.
     * @return Сообщение о результате операции.
     */
    @Override
    public String addCategory(String name) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            return "Категория с названием '" + name + "' уже существует.";
        }

        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);

        return "Категория " + name + " успешно добавлена как корневая.";
    }

    /**
     * Отображает дерево категорий.
     *
     * @return Строковое представление дерева категорий.
     */
    @Override
    public String viewTree() {
        List<Category> rootCategories = categoryRepository.findAll()
                .stream()
                .filter(category -> category.getParent() == null)
                .toList();

        if (rootCategories.isEmpty()) {
            return "Дерево категорий пусто.";
        }

        StringBuilder tree = new StringBuilder("Дерево категорий:\n");
        for (Category root : rootCategories) {
            tree.append(BuildTree.buildTree(root, 0));
        }
        return tree.toString();
    }

    /**
     * Удаляет категорию.
     *
     * @param name Название категории для удаления.
     * @return Сообщение о результате операции.
     */
    @Override
    public String removeCategory(String name) {
        Optional<Category> categoryOpt = categoryRepository.findByName(name);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            if (category.getParent() != null) {
                Category parent = category.getParent();
                parent.getChildren().remove(category);
                categoryRepository.save(parent);
            }
            categoryRepository.delete(category);
            return "Категория " + name + " успешно удалена";
        }
        return "Категория " + name + " не найдена";
    }
}

