package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Chat;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.Session;

import java.util.List;

public class FillingChatHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        FillingChatRequest chatRequest = ctx.bodyAsClass(FillingChatRequest.class);

        //Получаем id создателя запроса(и чата)
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        // Новое имя
        QueryBuilder<Chat, Integer> statementBuilder = Database.getInstance().getChatDao().queryBuilder();
        statementBuilder.where().eq("id", chatRequest.getId());
        List<Chat> chats = Database.getInstance().getChatDao().query(statementBuilder.prepare());

        chats.get(0).setName(chatRequest.getName());

        //Заполняем описание
        chats.get(0).setBio(chatRequest.getBio());

        Database.getInstance().getChatDao().createOrUpdate(chats.get(0));

        // отправляем всем пользователям чата
        QueryBuilder<Link, Integer> statementBuilderLink = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilderLink.where().eq("chat_id", chatRequest.getId());

        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilderLink.prepare());

        for(Link link: links){
            Gson gson = new Gson();
            Sse.getInstance().getClient(link.getUserId()).sendEvent("newChat", "edit chat");
        }

        ctx.json("Success");
    }
    static class FillingChatRequest {

        private Integer id;
        private String bio;
        private String name;

        public Integer getId() {
            return id;
        }
        public String getBio() {
            return bio;
        }
        public String getName() {
            return name;
        }

    }
}
