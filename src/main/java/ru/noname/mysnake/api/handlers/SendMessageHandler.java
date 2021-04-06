package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.*;

import java.util.Date;
import java.util.List;

public class SendMessageHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        SendMessageRequest sendMessageRequest = ctx.bodyAsClass(SendMessageHandler.SendMessageRequest.class);

        //Получаем id создателя сообщения
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        //Создаем ссобщение и добавляем в базу
        Message message = new Message(session.getUserId(), sendMessageRequest.getChatId(), sendMessageRequest.getMessage(), new Date());
        Database.getInstance().getMessageDao().create(message);

        // Получаем чат-пользователь
        QueryBuilder<Link, Integer> statementBuilderLink = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilderLink.where().eq("chat_id", sendMessageRequest.getChatId());
        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilderLink.prepare());

        for(Link link: links){

            Gson gson = new Gson();
            Sse.getInstance().getClient(link.getUserId()).sendEvent("message", gson.toJson(message));
        }
    }

    static class SendMessageRequest {

        private Integer chatId;
        private String message;

        public Integer getChatId() {
            return chatId;
        }
        public String getMessage() {
            return message;
        }

    }

    /*
            DialogueMessage message = ctx.bodyAsClass(DialogueMessage.class);
            Integer idUser = sessionAndNames.get(message.getSessionId());
            Integer chatId = database.findChatByName(message.getChatName());

            database.insertMessage(idUser, chatId, "date", message.getMessage());

            String name = database.findUserById(idUser);
            message.setSessionId(name);

            for(Integer userId : database.getUsersFromChat(chatId))
            {
                Gson gson = new Gson();
                clients.get(userId).sendEvent("message", gson.toJson(message));
            }
     */
}
