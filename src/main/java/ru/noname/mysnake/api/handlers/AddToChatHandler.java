package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.User;

import java.util.List;

public class AddToChatHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        AddToChatRequest addToChatRequest = ctx.bodyAsClass(AddToChatRequest.class);

        QueryBuilder<User, Integer> statementBuilder = Database.getInstance().getUserDao().queryBuilder();
        statementBuilder.where().eq("name", addToChatRequest.getUserName());

        List<User> users = Database.getInstance().getUserDao().query(statementBuilder.prepare());

        Database.getInstance().getLinkDao().create(new Link(addToChatRequest.getChatId(), users.get(0).getId()));

        Gson gson = new Gson();
        Sse.getInstance().getClient(users.get(0).getId()).sendEvent("newChat", "add to chat");

        ctx.json("success");
    }

    static class AddToChatRequest {

        private Integer chatId;
        private String userName;

        public Integer getChatId() {
            return chatId;
        }

        public String getUserName() {
            return userName;
        }
    }
}
