package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

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

        if (users.isEmpty()) {
            User user = new User(userRequest.getName(), userRequest.getPassword());
            Database.getInstance().getUserDao().create(user);
            users.add(user);
        }


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


        /*Pair<String, Integer> pair = database.findUser(userRequest.getName(), userRequest.getPassword());
        String sessionId = pair.component1();

        // нужно получить ай-ди юзера
        sessionAndNames.put(sessionId, pair.component2());

        // при входе отправляем пользователю все его чаты
        List<String> chats = new LinkedList<>();
        for(Integer chatId : database.getChatFromUser(pair.component2()))
        {
            chats.add(database.findChatById(chatId));
            // эти чаты надо отправлять клиенту для отображения
        }

        HashMap<String, Object> data = new HashMap<>();
        Session session = new Session();
        session.setsessionId(sessionId);
        data.put("session", session);
        data.put("chats", chats);
        ctx.json(data);*/
    }
}
