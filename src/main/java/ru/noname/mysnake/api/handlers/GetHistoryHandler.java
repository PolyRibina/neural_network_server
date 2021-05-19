package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.models.ChatRequest;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Message;
import ru.noname.mysnake.db.models.Session;

import java.util.List;

public class GetHistoryHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        ChatRequest getHistoryRequest = ctx.bodyAsClass(ChatRequest.class);

        //Получаем id пользователя
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        // Получаем сообщения по чату
        QueryBuilder<Message, Integer> statementBuilderMess = Database.getInstance().getMessageDao().queryBuilder();
        statementBuilderMess.where().eq("chat_id", getHistoryRequest.getChatId());
        List<Message> message = Database.getInstance().getMessageDao().query(statementBuilderMess.prepare());

        ctx.json(message);
    }
}
