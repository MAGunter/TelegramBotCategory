package com.test.telegrambot.utils;


import com.test.telegrambot.entity.Category;

public class DataUtils {

    // Transient Categories
    public static Category getElectronicsTransient() {
        return Category.builder()
                .name("Electronics")
                .parent(null)
                .build();
    }

    public static Category getShopTransient() {
        return Category.builder()
                .name("Shop")
                .parent(null)
                .build();
    }

    public static Category getSmartphonesTransient() {
        Category electronics = getElectronicsTransient();
        Category smartphones = Category.builder()
                .name("Smartphones")
                .parent(electronics)
                .build();
        electronics.getChildren().add(smartphones);
        return smartphones;
    }

    public static Category getLaptopsTransient() {
        Category electronics = getElectronicsTransient();
        Category laptops = Category.builder()
                .name("Laptops")
                .parent(electronics)
                .build();
        electronics.getChildren().add(laptops);
        return laptops;
    }

    public static Category getSamsungTransient() {
        Category smartphones = getSmartphonesTransient();
        Category samsung = Category.builder()
                .name("Samsung")
                .parent(smartphones)
                .build();
        smartphones.getChildren().add(samsung);
        return samsung;
    }

    public static Category getAppleTransient() {
        Category smartphones = getSmartphonesTransient();
        Category apple = Category.builder()
                .name("Apple")
                .parent(smartphones)
                .build();
        smartphones.getChildren().add(apple);
        return apple;
    }

    public static Category getGamingLaptopsTransient() {
        Category laptops = getLaptopsTransient();
        Category gamingLaptops = Category.builder()
                .name("Gaming Laptops")
                .parent(laptops)
                .build();
        laptops.getChildren().add(gamingLaptops);
        return gamingLaptops;
    }

    // Persisted Categories
    public static Category getElectronicsPersisted() {
        return Category.builder()
                .id(1L)
                .name("Electronics")
                .parent(null)
                .build();
    }

    public static Category getShopPersisted() {
        return Category.builder()
                .name("Shop")
                .parent(null)
                .build();
    }

    public static Category getSmartphonesPersisted() {
        Category electronics = getElectronicsPersisted();
        Category smartphones = Category.builder()
                .name("Smartphones")
                .parent(electronics)
                .build();
        electronics.getChildren().add(smartphones);
        return smartphones;
    }

    public static Category getSamsungPersisted() {
        Category smartphones = getSmartphonesPersisted();
        Category samsung = Category.builder()
                .name("Samsung")
                .parent(smartphones)
                .build();
        smartphones.getChildren().add(samsung);
        return samsung;
    }
}

