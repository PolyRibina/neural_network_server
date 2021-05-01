package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.sse.SseClient;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Session;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class SseHandler implements Consumer<SseClient> {

    @Override
    public void accept(SseClient client){
        try{
            acceptClient(client);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void acceptClient(SseClient client) throws SQLException {

        // Находим пользователя по сессии
        QueryBuilder<Session, Integer> statementBuilder = Database.getInstance().getSessionDao().queryBuilder();
        statementBuilder.where().eq("uuid", client.ctx.queryParam("sessionId"));
        List<Session> sessions = Database.getInstance().getSessionDao().query(statementBuilder.prepare());

        Sse.getInstance().addClient(sessions.get(0).getUserId(), client);

        client.sendEvent("connect", "Test sse");

        HashMap<Integer, SseClient> users =  Sse.getInstance().getAllClient();

        Gson gson = new Gson();

        for(SseClient clientSse: users.values()){
            clientSse.sendEvent("newUser", "user online");
        }
        //Sse.getInstance().getClient(sessions.get(0).getUserId()).sendEvent("isOnline", "yes");
        client.onClose(() ->  {
            Sse.getInstance().removeClient(sessions.get(0).getUserId());
            for(SseClient clientSse: users.values()){
                clientSse.sendEvent("newUser", "user not online");
            }
        } );

    }
    /*
        //String session = client.ctx.queryParam("sessionId");
            System.out.println(client.ctx.queryParam("sessionId"));

            Integer clientId = sessionAndNames.get(client.ctx.queryParam("sessionId"));

            Sse.getInstance().addClient(clientId, client);

            client.sendEvent("connect", "Test sse");

            // Восстановление истории
            //for(Integer chatId : database.getChatFromClient(clientId))
            //{
            //    for(ru.noname.mysnake.DialogueMessage mess : database.getChatMessage(chatId))
            //    {
            //        Gson gson = new Gson();
            //        client.sendEvent("message", gson.toJson(mess));
            //    }
            //}

            //storage client.sendEvent("message", ctx.body());
            //
            client.onClose(() -> System.out.println("Пользователь отключился"));
         */
}
