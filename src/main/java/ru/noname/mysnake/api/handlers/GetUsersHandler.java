package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.User;

import java.util.List;

public class GetUsersHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        QueryBuilder<User, Integer> statementBuilder = Database.getInstance().getUserDao().queryBuilder();
        List<User> users = Database.getInstance().getUserDao().query(statementBuilder.prepare());

        for(User user: users){
            user.setOnline(Sse.getInstance().getClient(user.getId()) != null);
        }
        ctx.json(users);
    }
}
