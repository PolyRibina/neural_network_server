package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

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

        //Заполняем таблицу ссылок чат-пользователь
        List<Link> links = new LinkedList<>();

        String[] usersStr = createChatRequest.getUsersNames().split( ", ");

        List<User> users = new LinkedList<>();

        QueryBuilder<User, Integer> statementBuilder = Database.getInstance().getUserDao().queryBuilder();
        for(String userName: usersStr){
            statementBuilder.where().eq("name", userName);
            users.addAll(Database.getInstance().getUserDao().query(statementBuilder.prepare()));
        }

        QueryBuilder<Link, Integer> statementBuilderLink = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilderLink.where().eq("user_id", session.getUserId());
        List<Link> chatLinks = Database.getInstance().getLinkDao().query(statementBuilderLink.prepare());

        Boolean isChatCreated = false;
        for(Link link: chatLinks){
            QueryBuilder<Link, Integer> statementBuilderUserInChat = Database.getInstance().getLinkDao().queryBuilder();
            statementBuilderUserInChat.where().eq("chat_id", link.getChatId());
            List<Link> usersInChat = Database.getInstance().getLinkDao().query(statementBuilderUserInChat.prepare());
            for(Link linkUsers: usersInChat){
                if(users.size() == 1 && users.get(0).getId()==linkUsers.getUserId()){
                    QueryBuilder<Chat, Integer> statementBuilderChat = Database.getInstance().getChatDao().queryBuilder();
                    statementBuilderChat.where().eq("id", linkUsers.getChatId());
                    List<Chat> chat = Database.getInstance().getChatDao().query(statementBuilderChat.prepare());
                    if(chat.get(0).getIsPrivateDialog()){
                        isChatCreated = true;
                        ctx.json(chat);
                        break;
                    }
                }
            }
            if (isChatCreated)
                break;
        }

        if (!isChatCreated){
            //Создаем чат
            Chat chat = new Chat(createChatRequest.getName());
            chat.setIsPrivateDialog(createChatRequest.getIsPrivate());
            Database.getInstance().getChatDao().create(chat);



            //List<Integer> ids = new LinkedList<>();
            links.add(new Link(chat.getId(), session.getUserId(), true));
            for(User user: users){
                links.add(new Link(chat.getId(), user.getId()));
            }

            Database.getInstance().getLinkDao().create(links);

            Refresh.doRefresh(links, "newChat", "new chat");

            ctx.json(chat.getId());
        }

    }

    static class CreateChatRequest {

        private String name;
        private String usersNames;
        private Boolean isPrivate;

        public String getName() {
            return name;
        }

        public String getUsersNames() {
            return usersNames;
        }

        public Boolean getIsPrivate() {
            return isPrivate;
        }
    }
}
