package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.db.models.Session;

import java.io.File;


public class AvatarChatUploadHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        UploadedFile file = ctx.uploadedFile("avatar");
        FileUtils.copyInputStreamToFile(file.getContent(), new File("avatars/chatId=" + ctx.queryParam("chatId") + ".jpeg"));
        ctx.json("Success");
    }
}
