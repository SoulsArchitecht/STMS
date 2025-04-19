# Task Management System - Spring Boot & PostgreSQL

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

Система управления задачами с аутентификацией и авторизацией пользователей, поддерживающая роли администратора и обычных пользователей.

## 🚀 Технологии

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** + JWT
- **PostgreSQL**
- **Docker** & Docker Compose
- **OpenAPI 3.0** (Swagger UI)
- **Lombok**

## 📋 Требования к системе

- Docker 20.10+
- Docker Compose 1.29+
- Доступ к портам:
  - `8080` (приложение)
  - `5433` (PostgreSQL)

## 🐳 Запуск проекта через Docker Compose

1. **Проверьте установку Docker**:

```bash```
docker --version
docker-compose --version

    Клонируйте репозиторий (если нужно):

```bash```

git clone <ваш-репозиторий>
cd <папка-проекта>

    Запустите проект:

```bash```

docker-compose up --build

    При первом запуске используйте --build для сборки образов

    Доступ к сервисам после запуска:

Сервис	URL
Приложение	http://localhost:8080
Swagger UI	http://localhost:8080/swagger-ui
PostgreSQL	localhost:5433

    Остановка:

```bash```

docker-compose down

------------------------------------------

⚙️ Конфигурация

Основные настройки в docker-compose.yml:
yaml

```services:
  app:
    environment:
      SPRING_DATASOURCE_URL: "jdbc:postgresql://db:5433/stms_db"
      SPRING_DATASOURCE_USERNAME: "username"
      SPRING_DATASOURCE_PASSWORD: "password"
      JWT_SECRET: "your-secret-key"
      JWT_EXPIRATION: "86400000" # 24 часа

      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: root
```


------------------------------------------

🗃️ Инициализация БД

При первом запуске автоматически выполняются liquibase SQL-скрипты из:

src/main/resources/db/migration/


------------------------------------------

🔐 Аутентификация

    Получение JWT токена:

http

POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "admin@admin.com",
  "password": "admin123"
}

    Использование токена:

Authorization: Bearer (ваш-token)


------------------------------------------

👥 Тестовые пользователи

Роль          |  Email            |Пароль

* Администратор	|  admin@admin.com	| admin123

* Пользователь	  |  1user@user.com	  | user1234


------------------------------------------

📚 Swagger документация

После запуска доступна интерактивная документация:

    Swagger UI: http://localhost:8080/swagger-ui

    OpenAPI spec: http://localhost:8080/v3/api-docs


------------------------------------------    

📂 Структура проекта

```
src/
├── main/
│   ├── java/
│   │   └── com/example/taskmanagement/
│   │       ├── config/          - Конфигурация
│   │       ├── controller/      - REST контроллеры
│   │       ├── dto/             - Data Transfer Objects
│   │       ├── entity/          - JPA сущности
│   │       ├── exception/       - Обработка ошибок
│   │       ├── mapper/          - Мапперы
│   │       ├── repository/      - Репозитории
│   │       ├── security/        - Spring Security
│   │       ├── service/         - Бизнес-логика
│   │       └── TaskManagementApplication.java
│   └── resources/
│       ├── db/migration/        - liquibase SQL миграции
│       ├── application.yml      - Настройки
└── test/                        - Тесты
```


------------------------------------------

⚠️ Возможные проблемы

Проблема	: Решение

* Порт занят	: Измените порт в docker-compose.yml
* Ошибки БД	: Проверьте логи: docker-compose logs db
* Миграции не применяются	: Удалите том: docker-compose down -v
* Проблемы с JWT	: Проверьте заголовок Authorization


------------------------------------------

📝 Логирование

Просмотр логов:
`bash`

* Логи приложения:
docker-compose logs app

* Логи PostgreSQL:
docker-compose logs db


------------------------------------------

🧹 Очистка

Полная остановка с удалением данных:
`bash`

docker-compose down -v

-------------------------------------------

### Приложение создано в демо-режиме. Полная документация, полная функциональность, контейнерное тестирование и прочие плюшки будут доступны только в платном режиме ;)

по всем вопросам и предложениям писать владельцу репозитория. Email: sshibkodev@gmail.com
