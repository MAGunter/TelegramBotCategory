# Telegram Category Bot

Добро пожаловать в **Telegram Category Bot**! Этот бот поможет вам выстроить категории, а также работать с Excel-файлами.

## 🚀 Функциональность
- Создание иерархии категорий
- Просмотр дерева категорий
- Экспорт данных в Excel
- Авторизация и управление пользователями

## 🛠 Технологии
- **Java 17+**
- **Spring Boot**
- **PostgreSQL** (но можно использовать любую БД)
- **Telegram Bot API**

## 🏗 Установка и запуск
### 1️⃣ Создание бота в Telegram
Перейдите в **@BotFather** и создайте нового бота. Он выдаст вам **токен**, который понадобится позже.

### 2️⃣ Настройка базы данных (PostgreSQL)
Создайте базу данных и выполните следующие SQL-команды:

```sql
CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    parent_id INTEGER REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    chat_id VARCHAR(255)
);
```

### 3️⃣ Конфигурация `application.properties`
Добавьте в `src/main/resources/application.properties` данные вашего бота и базы данных:

```properties
telegram.bot.token=ВАШ_ТОКЕН_БОТА
spring.datasource.url=jdbc:postgresql://localhost:5432/ВАША_БД
spring.datasource.username=ВАШ_ПОЛЬЗОВАТЕЛЬ
spring.datasource.password=ВАШ_ПАРОЛЬ
```

### 4️⃣ Запуск приложения
Запустите Spring Boot приложение:
```sh
mvn spring-boot:run
```

## 🔑 Авторизация и роли
При первом запуске вам будет назначена роль **User** с доступом к командам:
- `/download` – Скачать данные
- `/help` – Получить справку
- `/viewTree` – Посмотреть дерево категорий
- `/login` – Войти в систему

Чтобы получить права **администратора**, выполните команду:
```sh
/login admin admin
```

## 📩 Контакты
Если у вас есть вопросы или предложения, пишите мне! 😎
- maratarslan0@gmail.com

