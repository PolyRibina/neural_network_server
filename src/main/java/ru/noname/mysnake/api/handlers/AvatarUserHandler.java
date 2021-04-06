package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UploadedFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.db.models.Session;

import java.io.File;
import java.io.FileInputStream;


public class AvatarUserHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        String userId = ctx.queryParam("userId");
        FileInputStream inputFile = new FileInputStream("avatars/userId=" + userId + ".jpeg");
        ctx.result (inputFile);

    }
}
