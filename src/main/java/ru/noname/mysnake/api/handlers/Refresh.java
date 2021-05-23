package ru.noname.mysnake.api.handlers;

import ru.noname.mysnake.api.Sse;
import ru.noname.mysnake.db.models.Link;

import java.util.List;

public class Refresh {
    public static void doRefresh(List<Link> links, String event, String data) throws Exception {

        for(Link link: links){
            try {
                Sse.getInstance().getClient(link.getUserId()).sendEvent(event, data);
            }
            catch (NullPointerException e){
                System.out.println("Клиент " + link.getUserId() + " не в сети, поэтому обновление не произошло.");
            }
        }
    }
}
