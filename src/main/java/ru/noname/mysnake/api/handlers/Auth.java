package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Session;

import io.javalin.http.Context;
import java.util.List;

public class Auth {
    public static Session getSession(Context ctx) throws Exception {

        //Получаем id создателя запроса(и чата)
        QueryBuilder<Session, Integer> statementBuilder = Database.getInstance().getSessionDao().queryBuilder();
        statementBuilder.where().eq("uuid", ctx.req.getHeader("SessionId")); // получаем из Header
        List<Session> sessions = Database.getInstance().getSessionDao().query(statementBuilder.prepare());

        if (sessions.isEmpty()) {
            return null;
        }
        return sessions.get(0);
    }
}
