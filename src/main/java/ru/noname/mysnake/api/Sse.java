package ru.noname.mysnake.api;

import io.javalin.http.sse.SseClient;
import java.util.HashMap;

public class Sse {

    HashMap<Integer, SseClient> clients = new HashMap<>(); // userId - client

    // Добавление пользователя
    public void addClient(Integer id, SseClient client){
        clients.put(id, client);
    }

    // Получение пользователя
    public SseClient getClient(Integer id){
        return clients.get(id);
    }

    // Получение всех пользователей
    public HashMap<Integer, SseClient> getAllClient(){
        return clients;
    }

    // Удаление пользователя
    public void removeClient(Integer userId){
        clients.remove(userId);
    }

    private static Sse sse;

    public static Sse getInstance() {
        if (sse == null) {
            sse = new Sse();
        }
        return sse;
    }

}
