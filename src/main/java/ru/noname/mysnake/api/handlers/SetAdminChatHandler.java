package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.models.UserChatRequest;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Link;

import java.util.List;

public class SetAdminChatHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        UserChatRequest setAdminChatRequest = ctx.bodyAsClass(UserChatRequest.class);

        QueryBuilder<Link, Integer> statementBuilder = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilder.where().eq("chat_id", setAdminChatRequest.getChatId());

        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilder.prepare());

        for(Link link: links){

            if(link.getUserId()==setAdminChatRequest.getUserId()){
                new LeaveFromChatHandler().handle(ctx);
                Database.getInstance().getLinkDao().create(new Link(link.getChatId(), link.getUserId(), true));
            }
        }

        ctx.json("success");
    }
}
