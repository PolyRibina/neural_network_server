package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.api.models.UserChatRequest;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Link;

import java.util.List;

public class LeaveFromChatHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        UserChatRequest leaveFromChatRequest = ctx.bodyAsClass(UserChatRequest.class);

        QueryBuilder<Link, Integer> statementBuilder = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilder.where().eq("chat_id", leaveFromChatRequest.getChatId());

        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilder.prepare());

        for(Link link: links){

            if(link.getUserId()==leaveFromChatRequest.getUserId()){
                Database.getInstance().getLinkDao().executeRaw("DELETE FROM links WHERE chat_id = '" +link.getChatId()+ "' AND user_id = '" +link.getUserId()+ "'");
                Sse.getInstance().getClient(link.getUserId()).sendEvent("newChat","leave from chat");
            }
        }

        ctx.json("success");
    }
}
