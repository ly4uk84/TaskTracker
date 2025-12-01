Быстрый запуск
bash
# 1. Запуск PostgreSQL
docker-compose up -d

# 2. Запуск приложения
./mvnw spring-boot:run
# или mvn spring-boot:run
Адрес API: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html​

Последовательность использования
text
1. POST /api/auth/register  → первый пользователь = ADMIN
   { "email": "admin@test.com", "password": "password" }

2. POST /api/auth/login     → получить JWT токен
   { "email": "admin@test.com", "password": "password" }

3. В Swagger: Authorize → Bearer <токен>

4. Работа с задачами/группами
5. Админ-функции (если ADMIN)
   JWT Аутентификация
   text
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
 
   Эндпоинты
   Авторизация (доступ всем)
   Метод	Путь	Описание
   POST	/api/auth/register	{ "email": "...", "password": "..." }
   POST	/api/auth/login	{ "email": "...", "password": "..." } → token
   GET	/api/auth/logout	Очистить токен на клиенте
 
   Задачи (USER/ADMIN)
   Метод	Путь	Описание
   GET	/api/tasks	Свои задачи
   POST	/api/tasks	{ "title": "...", "description": "...", "groupId": 1 }
   GET	/api/tasks/{id}	Задача по ID
   PUT	/api/tasks/{id}	{ "title": "...", "status": "DONE", "groupId": 1 }
   DELETE	/api/tasks/{id}	Удалить задачу

   Группы задач (USER/ADMIN)
   Метод	Путь	Описание
   GET	/api/task-groups	Свои группы
   POST	/api/task-groups	{ "name": "Математика" }
   PUT	/api/task-groups/{id}	{ "name": "новое имя" }
   DELETE	/api/task-groups/{id}	Удалить группу

   Админ (только ADMIN)
   Метод	Путь	Описание
   GET	/api/admin/users	Все пользователи
   GET	/api/admin/tasks	Все задачи
   GET	/api/admin/users/{id}/tasks	Задачи пользователя
   GET	/api/admin/stats	Статистика (JPQL)

Swagger UI
http://localhost:8080/swagger-ui.html

Authorize → Bearer <токен из login>

Первый зарегистрированный = ADMIN