package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.sse.SseClient;
import org.jetbrains.annotations.NotNull;

import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.api.models.UserRequest;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Session;
import ru.noname.mysnake.db.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AuthHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        UserRequest userRequest = ctx.bodyAsClass(UserRequest.class);

        QueryBuilder<User, Integer> statementBuilder = Database.getInstance().getUserDao().queryBuilder();
        statementBuilder.where().eq("name", userRequest.getName());

        List<User> users = Database.getInstance().getUserDao().query(statementBuilder.prepare());

        if (userRequest.getPassword().equals(users.get(0).getPassword())) {
            UUID uuid = UUID.randomUUID();
            Session session = new Session(uuid.toString(), users.get(0).getId());
            Database.getInstance().getSessionDao().createOrUpdate(session);
            HashMap<String, Object> resp = new HashMap<>();
            resp.put("session_id", uuid.toString());
            resp.put("user", users.get(0));
            ctx.json(resp);
        } else {
            ctx.status(403);
        }
    }
}
