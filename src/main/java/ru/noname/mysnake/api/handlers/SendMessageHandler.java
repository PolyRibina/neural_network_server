package ru.noname.mysnake.api.handlers;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.QueryBuilder;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.db.Database;
import ru.noname.mysnake.db.models.*;

import java.io.File;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

public class SendMessageHandler implements Handler {

    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        //Получаем id создателя сообщения
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        //Создаем ссобщение и добавляем в базу
        Message message = new Message(session.getUserId(), parseInt(ctx.queryParam("chatId")), ctx.queryParam("message"), new Date());
        Database.getInstance().getMessageDao().create(message);

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
            FileUtils.copyInputStreamToFile(file.getContent(), new File("fileInMessage/messageId=" + message.getId() + myType));
            //http://localhost:8000/file-in-message?messageId=26&type=image

            message.setFileInMessage("file-in-message?messageId=" + message.getId()+ "&type=" + ctx.queryParam("type"));
            message.setType(ctx.queryParam("type"));
            Database.getInstance().getMessageDao().createOrUpdate(message);
        }


        // Получаем чат-пользователь
        QueryBuilder<Link, Integer> statementBuilderLink = Database.getInstance().getLinkDao().queryBuilder();
        statementBuilderLink.where().eq("chat_id", parseInt(ctx.queryParam("chatId")));
        List<Link> links = Database.getInstance().getLinkDao().query(statementBuilderLink.prepare());

        Gson gson = new Gson();
        Refresh.doRefresh(links, "message", gson.toJson(message));

        ctx.json("Success");
    }
}
