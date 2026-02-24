# Banking API

> REST API банковского приложения - Pet-проект

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.3-brightgreen?style=flat-square&logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-brightgreen?style=flat-square&logo=springsecurity)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-3.9-red?style=flat-square&logo=apachemaven)

---
Banking API - это полноценный backend сервис банковского приложения.  
Реализована регистрация пользователей, авторизация через JWT токены,  
управление счетами и переводы между ними с полной историей транзакций.
---
| Технология           | Описание |
|----------------------|----------|
| **Java 21**          | Основной язык |
| **Spring Boot 4.0.3** | Фреймворк приложения |
| **Spring Security**  | Аутентификация и авторизация |
| **JWT**   | Stateless токены |
| **Spring Data JPA**  | Работа с базой данных |
| **Hibernate**        | ORM маппинг |
| **PostgreSQL**       | Основная база данных |
| **H2**               | In-memory БД для разработки |
| **Lombok**           | Уменьшение boilerplate кода |
| **Maven**            | Сборка проекта |

---

## Функциональность

- Регистрация и вход с выдачей JWT токена
- Создание банковских счетов
- Пополнение счёта
- Переводы между счетами с проверкой баланса
- История транзакций с пагинацией
- Защита эндпоинтов - доступ только к своим счетам
- Запись каждой операции в историю транзакций

---

## API Endpoints

### Auth
| Метод | URL | Описание |
|-------|-----|----------|
| `POST` | `/api/auth/register` | Регистрация нового пользователя |
| `POST` | `/api/auth/login` | Вход и получение токена |

### Accounts
| Метод | URL | Описание |
|-------|-----|----------|
| `POST` | `/api/accounts` | Создать новый счёт |
| `GET` | `/api/accounts` | Получить все свои счета |
| `POST` | `/api/accounts/{id}/deposit` | Пополнить счёт |
| `POST` | `/api/accounts/transfer` | Перевод между счетами |
| `GET` | `/api/accounts/{id}/transactions` | История операций |

---

## Демонстрация

### Регистрация и получение токена
![Регистрация](screenshots/register.png)

### Логин
![Логин](screenshots/login.png)

### Создание счёта
![Создание счёта](screenshots/create-account.png)

### Пополнение счёта
![Депозит](screenshots/deposit.png)

### Перевод между счетами
![Перевод](screenshots/transfer.png)

### Счёты
![Счета](screenshots/getAccounts.png)

---

## Безопасность

- Пароли хешируются через **BCrypt**
- Аутентификация через **JWT Bearer токен**
- Каждый запрос проходит через **JwtAuthFilter**
- Пользователь имеет доступ **только к своим счетам**
- **Stateless** сессии - сервер не хранит состояние
---

## Запуск проекта

### С H2 (для разработки - ничего не нужно устанавливать)

```bash
git clone https://github.com/Michael-merlot/banking-api.git
cd banking-api
mvn spring-boot:run
```

Приложение запустится на `http://localhost:8080`  
H2 консоль доступна на `http://localhost:8080/h2-console`

### Переменные окружения

```yaml
jwt.secret=ключ
jwt.expiration=86400000
```

---