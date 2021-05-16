package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.Message;
import ru.noname.mysnake.db.models.Session;

import java.util.List;

public class DeleteMessageHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        //Получаем id создателя сообщения
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        DeleteMessageRequest deleteMessageRequest = ctx.bodyAsClass(DeleteMessageRequest.class);

        QueryBuilder<Message, Integer> statementBuilder = Database.getInstance().getMessageDao().queryBuilder();
        statementBuilder.where().eq("id", deleteMessageRequest.getMessageId());

        List<Message> message = Database.getInstance().getMessageDao().query(statementBuilder.prepare());

        Database.getInstance().getMessageDao().executeRaw("DELETE FROM messages WHERE id = " +message.get(0).getId());
        Gson gson = new Gson();
        Sse.getInstance().getClient(session.getUserId()).sendEvent("message","delete message from chat");
    }
    static class DeleteMessageRequest {

        private Integer messageId;

        public Integer getMessageId() {
            return messageId;
        }
    }
}
