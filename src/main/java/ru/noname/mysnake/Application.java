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

        // Авторизация
        app.post("/auth", new AuthHandler());

        // Создание нового чата
        app.post("/new-chat", new CreateChatHandler());

        app.sse("/sse", new SseHandler());

        // Восстановление истории чата
        app.post("/get-history", new GetHistoryHandler());

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

        // Список чатов
        app.get("/get-chats", new GetChatsHandler());

        // Список пользователей
        app.get("/get-users", new GetUsersHandler());

        // Проверка на онлайн
        app.get("/online", new OnlineHandler());
    }
}
