package com.test.telegrambot.service.impl;

import com.test.telegrambot.entity.Category;
import com.test.telegrambot.repository.CategoryRepository;
import com.test.telegrambot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public String addCategory(String parent, String o){
        if(parent == null){
            Category ctg = new Category();
            ctg.setName(o);
            categoryRepository.save(ctg);
            return "Категория " + o + " успешно добавлена как корневая";
        }
        else{
            Optional<Category> parentCategory = categoryRepository.findByName(parent);
            if(parentCategory.isPresent()){
                Category parentName = parentCategory.get();
                Category ctg = new Category();
                ctg.setName(o);
                ctg.setParent(parentName);
                categoryRepository.save(ctg);
                return "Категория " + o + " добавлена, его родитель " + parent;
            }
            return "Родительская категория " + parent + " не найдена";
        }
    }

    @Override
    public String addCategory(String name) {
        Optional<Category> existingCategory = categoryRepository.findByName(name);
        if (existingCategory.isPresent()) {
            return "Категория с названием '" + name + "' уже существует.";
        }

        Category category = new Category();
        category.setName(name);
        categoryRepository.save(category);

        return "Категория '" + name + "' успешно добавлена как корневая.";
    }

}
