package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.Link;
import ru.noname.mysnake.db.models.Message;
import ru.noname.mysnake.db.models.Session;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.parseInt;

public class EditMessageHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        //Получаем id создателя сообщения
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        QueryBuilder<Message, Integer> statementBuilder = Database.getInstance().getMessageDao().queryBuilder();
        statementBuilder.where().eq("id", ctx.queryParam("messageId"));

        List<Message> message = Database.getInstance().getMessageDao().query(statementBuilder.prepare());

        message.get(0).setText(ctx.queryParam("messageText"));
        message.get(0).setIsEdited(true);
        message.get(0).setWasRead(false);

        if(!ctx.queryParam("type").equals("")){
            // Работа с загружаемым файлом
            UploadedFile file = ctx.uploadedFile("attach");

            String myType = "";
            if(ctx.queryParam("type").equals("image")){
                myType = ".jpeg";
            }
            else if (ctx.queryParam("type").equals("video")){
                myType = ".mp4";
            }
            else {
                myType = ".mp3";
            }
            FileUtils.copyInputStreamToFile(file.getContent(), new File("fileInMessage/messageId=" + ctx.queryParam("messageId") + myType));

            message.get(0).setFileInMessage("file-in-message?messageId=" + ctx.queryParam("messageId")+ "&type=" + ctx.queryParam("type"));
            message.get(0).setType(ctx.queryParam("type"));
        }
        else{
            message.get(0).setFileInMessage(null);
            message.get(0).setType(null);
        }

        Database.getInstance().getMessageDao().update(message.get(0));

        // Получаем список пользователей чата
        QueryBuilder<Link, Integer> statementBuilderLink = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilderLink.where().eq("chat_id", message.get(0).getChatId());
        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilderLink.prepare());

        // Получаем сообщения по чату
        QueryBuilder<Message, Integer> statementBuilderMess = Database.getInstance().getMessageDao().queryBuilder();
        statementBuilderMess.where().eq("chat_id", message.get(0).getChatId());
        List<Message> messages = Database.getInstance().getMessageDao().query(statementBuilderMess.prepare());

        for(Link link: links){

            Gson gson = new Gson();
            try {
                Sse.getInstance().getClient(link.getUserId()).sendEvent("deleteMessage", gson.toJson(messages));
            }
            catch (NullPointerException e){
                System.out.println("Клиент " + link.getUserId() + " не в сети, поэтому обновление не произошло.");
            }
        }

        ctx.json("success editing");
    }
}
