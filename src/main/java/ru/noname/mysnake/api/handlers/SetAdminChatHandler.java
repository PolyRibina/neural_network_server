package ru.noname.mysnake.api.handlers;

import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Link;

import java.util.List;

public class SetAdminChatHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        SetAdminChatRequest setAdminChatRequest = ctx.bodyAsClass(SetAdminChatRequest.class);

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

    static class SetAdminChatRequest {

        private Integer chatId;
        private Integer userId;

        public Integer getChatId() {
            return chatId;
        }

        public Integer getUserId() {
            return userId;
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
