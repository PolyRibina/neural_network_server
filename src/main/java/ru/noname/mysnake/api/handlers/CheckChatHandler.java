package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class CheckChatHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        CheckChatRequest checkChatRequest =  ctx.bodyAsClass(CheckChatRequest.class);
    }
    static class CheckChatRequest {

        private Integer id;
        private Date date;

        public Integer getId() {
            return id;
        }

        public Date getDate() {
            return date;
        }
    }
}
