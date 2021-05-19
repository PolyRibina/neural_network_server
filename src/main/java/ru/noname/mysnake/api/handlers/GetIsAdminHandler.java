package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.models.UserChatRequest;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.Session;

import java.util.List;

public class GetIsAdminHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        //Получаем id создателя сообщения
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        UserChatRequest getIsAdminRequest = ctx.bodyAsClass(UserChatRequest.class);

        QueryBuilder<Link, Integer> statementBuilder = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilder.where().eq("chat_id", getIsAdminRequest.getChatId());

        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilder.prepare());

        for(Link link: links){
            if(link.getUserId()==getIsAdminRequest.getUserId()){
                ctx.json(link.getIsAdmin());
            }
        }
    }
}
