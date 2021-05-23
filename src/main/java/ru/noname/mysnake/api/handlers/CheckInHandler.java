package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.models.UserRequest;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.User;

import java.util.List;

public class CheckInHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        UserRequest userRequest = ctx.bodyAsClass(UserRequest.class);

        QueryBuilder<User, Integer> statementBuilder = Database.getInstance().getUserDao().queryBuilder();
        statementBuilder.where().eq("name", userRequest.getName());

        List<User> users = Database.getInstance().getUserDao().query(statementBuilder.prepare());

        if (users.isEmpty()) {
            User user = new User(userRequest.getName(), userRequest.getPassword());
            Database.getInstance().getUserDao().create(user);
            users.add(user);
        } else {
            ctx.status(403);
        }

    }
}
