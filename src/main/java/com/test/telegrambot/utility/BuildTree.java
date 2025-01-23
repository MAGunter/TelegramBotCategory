package com.test.telegrambot.utility;

import com.test.telegrambot.entity.Category;

public class BuildTree {
    public static String buildTree(Category category, int level) {
        StringBuilder builder = new StringBuilder("    ".repeat(level) + "~ " + category.getName() + "\n");

        for(Category child : category.getChildren()){
            builder.append(buildTree(child, level + 1));
        }

        return builder.toString();
    }
}
