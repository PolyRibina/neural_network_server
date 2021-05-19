package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.sse.SseClient;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Session;

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

        client.onClose(() ->  {
            Sse.getInstance().removeClient(sessions.get(0).getUserId());
            for(SseClient clientSse: users.values()){
                clientSse.sendEvent("newUser", "user not online");
            }
        } );

    }
}
