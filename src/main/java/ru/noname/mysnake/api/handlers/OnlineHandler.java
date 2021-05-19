package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.models.Session;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class OnlineHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        //Получаем id создателя
        Session session = Auth.getSession(ctx);
        if(session == null){
            ctx.status(403);
            return;
        }

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable toRun = new Runnable() {
            public void run() {
                Sse.getInstance().getClient(session.getUserId()).sendEvent("isOnline", "yes");
            }
        };
        ScheduledFuture<?> handle = scheduler.scheduleAtFixedRate(toRun, 1, 3, TimeUnit.SECONDS);

        ctx.result("{}");
    }
}
