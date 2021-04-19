package ru.noname.mysnake.api.handlers;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.sse.SseClient;
import org.jetbrains.annotations.NotNull;
import ru.noname.mysnake.api.Sse;

import java.util.HashMap;

public class RefreshHandler implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        HashMap<Integer, SseClient> users =  Sse.getInstance().getAllClient();

        for(SseClient client: users.values()){
            client.sendEvent("refresh", "test");
        }

    }
}
