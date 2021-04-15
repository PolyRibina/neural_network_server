package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Chat;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.Message;
import ru.noname.mysnake.db.models.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GetUsersChatHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        GetUsersChatRequest getUsersChatRequest = ctx.bodyAsClass(GetUsersChatRequest.class);

        // Получаем сообщения по чату
        QueryBuilder<Link, Integer> statementBuilder = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilder.where().eq("chat_id", getUsersChatRequest.getChatId());
        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilder.prepare());

        List<Integer> usersId = new LinkedList<>();
        for(Link link: links){
            usersId.add(link.getUserId());
        }

        List<User> users = new LinkedList<>();
        for(Integer id: usersId){
            QueryBuilder<User, Integer> statementBuilderUser = Database.getInstance().getUserDao().queryBuilder();
            statementBuilderUser.where().eq("id", id);
            users.add(Database.getInstance().getUserDao().query(statementBuilderUser.prepare()).get(0));
        }

        HashMap<String, Object> userAdmin = new HashMap<>();

        userAdmin.put("usersInChat", users);
        userAdmin.put("is_admin", links);

        ctx.json(userAdmin);
    }
    static class GetUsersChatRequest {

        private Integer chatId;
        public Integer getChatId() {
            return chatId;
        }

    }
}
