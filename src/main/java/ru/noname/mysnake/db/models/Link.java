package ru.noname.mysnake.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "links")
public class Link {

    public Link(){

    }

    public Link(int chatId, int userId){
        this.chatId = chatId;
        this.userId = userId;
    }

    @DatabaseField(columnName = "chat_id", canBeNull = false)
    private int chatId;

    @DatabaseField(columnName = "user_id",canBeNull = false)
    private int userId;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
