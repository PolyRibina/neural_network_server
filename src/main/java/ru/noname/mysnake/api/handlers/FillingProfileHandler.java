package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Session;
import ru.noname.mysnake.db.models.User;

import java.util.List;

public class FillingProfileHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        FillingProfileRequest profileRequest = ctx.bodyAsClass(FillingProfileRequest.class);
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        // Новое имя
        QueryBuilder<User, Integer> statementBuilder = Database.getInstance().getUserDao().queryBuilder();
        statementBuilder.where().eq("id", session.getUserId());
        List<User> users = Database.getInstance().getUserDao().query(statementBuilder.prepare());

        users.get(0).setName(profileRequest.getName());

        //Заполняем описание
        users.get(0).setBio(profileRequest.getBio());

        Database.getInstance().getUserDao().createOrUpdate(users.get(0));

        ctx.json("Success");
    }
    static class FillingProfileRequest {

        private String bio;
        private String name;

        public String getBio() {
            return bio;
        }
        public String getName() {
            return name;
        }

    }
}
