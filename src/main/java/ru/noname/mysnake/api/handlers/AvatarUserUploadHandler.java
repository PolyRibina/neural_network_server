package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.db.models.Session;
import java.io.File;


public class AvatarUserUploadHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        UploadedFile file = ctx.uploadedFile("avatar");
        FileUtils.copyInputStreamToFile(file.getContent(), new File("avatars/userId=" + session.getUserId() + ".jpeg"));
        ctx.json("Success");
    }
}
