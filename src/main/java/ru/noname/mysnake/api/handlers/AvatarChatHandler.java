package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class AvatarChatHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        String chatId = ctx.queryParam("chatId");
        try {
            FileInputStream inputFile = new FileInputStream("avatars/chatId=" + chatId + ".jpeg");
            ctx.result(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("Чат " + chatId + " без аватарки.");
        }

    }
}
