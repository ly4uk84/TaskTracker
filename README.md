Быстрый запуск
bash
# 1. Запуск PostgreSQL
docker-compose up -d

# 2. Запуск приложения
./mvnw spring-boot:run
или mvn spring-boot:run

Адрес API: http://localhost:8080  
Swagger UI: http://localhost:8080/swagger-ui.html  
Первый зарегистрированный роль = ADMIN

# 3. Последовательность использования

1. POST /api/auth/register  → первый пользователь = ADMIN
   { "email": "admin@test.com", "password": "password" }
2. POST /api/auth/login     → получить JWT токен
   { "email": "admin@test.com", "password": "password" }
3. В Swagger: Authorize → Bearer <токен>
4. Работа с задачами/группами

#   4. Эндпоинты
Авторизация

| Метод | Эндпоинт           | Тело запроса                        | Ответ                            |
| ----- | ------------------ | ----------------------------------- | -------------------------------- |
| POST  | /api/auth/register | {"email": "...", "password": "..."} | {"token": "..."}                 |
| POST  | /api/auth/login    | {"email": "...", "password": "..."} | {"token": "..."}                 |
Задачи (USER/ADMIN)

| Метод  | Эндпоинт        | Тело запроса                                         | Описание        |
| ------ | --------------- | ---------------------------------------------------- | --------------- |
| GET    | /api/tasks      | -                                                    | Своизадачи      |
| POST   | /api/tasks      | {"title": "...", "description": "...", "groupId": 1} | Создать задачу  |
| GET    | /api/tasks/{id} | -                                                    | Задача по ID    |
| PUT    | /api/tasks/{id} | {"title": "...", "status": "DONE"}                   | Обновить задачу |
| DELETE | /api/tasks/{id} | -                                                    | Удалить задачу  |

Группы задач (USER/ADMIN)

| Метод  | Эндпоинт              | Тело запроса           | Описание       |
| ------ | --------------------- | ---------------------- | -------------- |
| GET    | /api/task-groups      | -                      | Своигруппы     |
| POST   | /api/task-groups      | {"name": "Математика"} | Создать группу |
| PUT    | /api/task-groups/{id} | {"name": "новое имя"}  | Переименовать  |
| DELETE | /api/task-groups/{id} | -                      | Удалить группу |

Админ (только ADMIN)

| Метод | Эндпоинт                    | Описание           |
| ----- | --------------------------- |--------------------|
| GET   | /api/admin/users            | Все пользователи   |
| GET   | /api/admin/tasks            | Все задачи         |
| GET   | /api/admin/users/{id}/tasks | Задачи пользователя |
| GET   | /api/admin/stats            | Статистика         |