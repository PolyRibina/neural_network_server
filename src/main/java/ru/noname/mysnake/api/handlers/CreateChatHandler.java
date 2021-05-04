package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.sse.SseClient;
import org.jetbrains.annotations.NotNull;

import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Chat;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.Session;
import ru.noname.mysnake.db.models.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CreateChatHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        CreateChatRequest createChatRequest = ctx.bodyAsClass(CreateChatRequest.class);

        //Получаем id создателя запроса(и чата)
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        //Создаем чат
        Chat chat = new Chat(createChatRequest.getName());
        Database.getInstance().getChatDao().create(chat);

        //Заполняем таблицу ссылок чат-пользователь
        List<Link> links = new LinkedList<>();

        String[] usersStr = createChatRequest.getUsersNames().split( ", ");

        List<User> users = new LinkedList<>();

        for(String userName: usersStr){
            QueryBuilder<User, Integer> statementBuilder = Database.getInstance().getUserDao().queryBuilder();
            statementBuilder.where().eq("name", userName);
            users.add(Database.getInstance().getUserDao().query(statementBuilder.prepare()).get(0));
        }

        //List<Integer> ids = new LinkedList<>();
        links.add(new Link(chat.getId(), session.getUserId(), true));
        for(User user: users){
            links.add(new Link(chat.getId(), user.getId()));
        }

        Database.getInstance().getLinkDao().create(links);

        for(Link link: links){
            Gson gson = new Gson();
            try {
                Sse.getInstance().getClient(link.getUserId()).sendEvent("newChat", "new chat");
            }
            catch (NullPointerException e){
                System.out.println("Клиент не в сети, поэтому обновление не произошло.");
            }

        }

        ctx.json(chat.getId());
    }

    static class CreateChatRequest {

        private String name;
        private String usersNames;

        public String getName() {
            return name;
        }

        public String getUsersNames() {
            return usersNames;
        }
    }

    /*

    Chat chat = ctx.bodyAsClass(Chat.class);

    // надоходим id пользователя по имени
    String str = chat.getUsers() + " " + chat.getCreator();
    String[] users = str.split(" ");
    List<Integer> ids = new LinkedList<>();
    for(String user : users)
    {
         Integer id = database.findUserByName(user);
         ids.add(id);
     }
     database.insertChat(chat.getChatName(), ids); // создаем чат
     */
}
