# Petstore API Test Project

## Описание проекта

Этот проект предназначен для тестирования API **Petstore** с помощью библиотеки **Rest Assured** и написания как позитивных, так и негативных тестов. Тесты покрывают такие действия, как создание заказа, получение заказа по ID, удаление заказа и проверку инвентаря.

Проект использует **JUnit 5** в качестве тестового фреймворка и **Rest Assured** для взаимодействия с API. Для генерации случайных данных используется библиотека **Faker**.

## Технологии и библиотеки

Проект написан на **Java** и использует следующие библиотеки и инструменты:

- **JUnit 5** — фреймворк для тестирования.
- **Rest Assured** — библиотека для тестирования REST API.
- **Hamcrest** — библиотека для удобного написания проверок в тестах.
- **Faker** — для генерации случайных данных.
- **Maven** — инструмент для управления зависимостями и сборки проекта.

### Основные тестовые классы:

- `StoreAppPositiveTest` — содержит позитивные тесты для API.
- `StoreAppNegativeTest` — содержит негативные тесты для проверки обработки ошибок API.

### Запуск тестов
Для запуска всех тестов выполните команду: **'mvn clean test'**

Для запуска позитивных тестов: **'mvn test -Dgroups="positive"'**

Для запуска негативных тестов: **'mvn test -Dgroups="negative"'**
