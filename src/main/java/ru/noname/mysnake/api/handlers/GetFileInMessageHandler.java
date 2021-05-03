package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class GetFileInMessageHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        String messageId = ctx.queryParam("messageId");

        String myType = "";
        if(ctx.queryParam("type").equals("image")){
            myType = ".jpeg";
        }
        else if (ctx.queryParam("type").equals("video")){
            myType = ".mp4";
        }
        else{
            myType = ".mp3";
        }
        try{

            FileInputStream inputFile = new FileInputStream("fileInMessage/messageId=" + messageId + myType);
            ctx.result(inputFile);
        }
        catch (FileNotFoundException e){
            System.out.println("Сообщение " + messageId + " без файла.");
        }
    }
}
