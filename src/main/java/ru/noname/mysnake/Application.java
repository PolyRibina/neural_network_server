package ru.noname.mysnake;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import ru.noname.mysnake.api.handlers.*;
import ru.noname.mysnake.db.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Application {
    public static void main(String[] args){
        Javalin app = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(8000);

        try {
            Database.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Path path = Paths.get("avatars");
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Отправка сообщения
        app.post("/send-message", new SendMessageHandler());

        // Редактирование сообщения
        app.post("/edit-message", new EditMessageHandler());

        // Чтение сообщения
        app.post("/read-message", new ReadMessageHandler());

        // Удаление сообщения
        app.post("/delete-message", new DeleteMessageHandler());

        // Получаем файл сообщения
        app.get("/file-in-message", new GetFileInMessageHandler());


        // Создание нового чата
        app.post("/new-chat", new CreateChatHandler());

        // Выход из чата
        app.post("/leave-from-chat", new LeaveFromChatHandler());

        // Добавление чата
        app.post("/add-to-chat", new AddToChatHandler());

        // Восстановление истории чата
        app.post("/get-history", new GetHistoryHandler());


        // Назначение админа чата
        app.post("/set-admin-chat", new SetAdminChatHandler());

        // Проверка пользователя на права админа
        app.post("/get-is-admin", new GetIsAdminHandler());



        // Загружаем картинку пользователя
        app.post("/avatar", new AvatarUserUploadHandler());

        // Получаем картинку пользователя
        app.get("/avatar", new AvatarUserHandler());

        // Загружаем картинку чата
        app.post("/avatar-chat", new AvatarChatUploadHandler());

        // Получаем картинку чата
        app.get("/avatar-chat", new AvatarChatHandler());


        //Заполняем описание профиля
        app.post("/profile", new FillingProfileHandler());

        //Заполняем описание чата
        app.post("/edit-chat", new FillingChatHandler());


        // Список чатов
        app.get("/get-chats", new GetChatsHandler());

        // Список пользователей
        app.get("/get-users", new GetUsersHandler());

        // Список пользователей чата
        app.post("/get-users-chat", new GetUsersChatHandler());


        // Авторизация
        app.post("/auth", new AuthHandler());

        // Проверка на онлайн
        app.get("/online", new OnlineHandler());

        // Sse
        app.sse("/sse", new SseHandler());
    }
}
