package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.query.In;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Chat;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.Session;

import java.util.LinkedList;
import java.util.List;

public class GetChatsHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        // Получаем чат-пользователь
        QueryBuilder<Link, Integer> statementBuilderLink = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilderLink.where().eq("user_id", session.getUserId());
        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilderLink.prepare());

        List<Integer> chatId = new LinkedList<>();
        for(Link link: links){
            chatId.add(link.getChatId());
        }

        List<Chat> chats = new LinkedList<>();
        for(Integer id: chatId){
            QueryBuilder<Chat, Integer> statementBuilderChat = Database.getInstance().getChatDao().queryBuilder();
            statementBuilderChat.where().eq("id", id);
            chats.add(Database.getInstance().getChatDao().query(statementBuilderChat.prepare()).get(0));
        }

        ctx.json(chats);
    }
}
