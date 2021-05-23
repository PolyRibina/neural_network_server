package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class AvatarUserHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        String userId = ctx.queryParam("userId");
        try{
            FileInputStream inputFile = new FileInputStream("avatars/userId=" + userId + ".jpeg");
            ctx.result(inputFile);
        }
        catch (FileNotFoundException e){
            System.out.println("Пользователь " + userId + " без аватарки.");
        }
    }
}
