# AstonSpringHomework

## Инструкция по запуску программы

1. В файл src/main/resources/application.yml на место 
`username` вставить свой логин почты, а на место `password` — код приложения.
Именно код приложения, а не простой пароль.

        mail:
            username: test@test.com
            password: 
2. Включить Docker
3. В терминале зайти в корневой каталог App. 
4. Ввести команду  

       docker-compose up -d
5. Убедиться, что в Docker подняты два контейнера — broker и zookeeper. Можно сделать
с помощью команды

       docker ps
6. Запустить приложение. Можно использовать команду

       mvn spring-boot:run 
7. Дополнительно: это приложение отправляет сообщение в Kafka. Прием сообщения можно проверить 
в другом приложении, которое можно запустить параллельно — в нем для запуска достаточно только подставить
   `username` и `password` в файле `application.yml`: https://github.com/Matiika/KafkaConsumerExample

## Список запросов

1. Создание нового пользователя 

       curl -X POST http://localhost:8080/api/users `
       -H "Content-Type: application/json" `
       -d '{"name": "test", "email": "test@test.com", "age": "33"}'
2. Просмотр всех пользователей

       curl -X GET http://localhost:8080/api/users
3. Редактирование пользователя. В конце нужно подставить его ID.
   
       curl -X PUT http://localhost:8080/api/users/1 `
       -H "Content-Type: application/json" `
       -d '{
       "email": "example@example.com"
       }'
4. Удаление пользователя. В конце нужно подставить его ID.
   
       curl -X DELETE http://localhost:8080/api/users/1
5. Отправка сообщения пользователю на почту. Для проверки нужно вставить свою реальную почту после `email=`. 
Параметр `eventType` может быть `CREATED` для уведомления о создании аккаунта или `DELETED` для уведомления
об удалении аккаунта.

       curl -X POST "http://localhost:8080/api/send-email" `
       -d "email=test@test.com" `
       -d "eventType=CREATED"
