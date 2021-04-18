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
    public Link(int chatId, int userId, boolean isAdmin){
        this.chatId = chatId;
        this.userId = userId;
        this.isAdmin = isAdmin;
    }

    @DatabaseField(columnName = "chat_id", uniqueCombo = true, canBeNull = false)
    private int chatId;

    @DatabaseField(columnName = "user_id", uniqueCombo = true, canBeNull = false)
    private int userId;

    @DatabaseField(columnName = "is_admin", canBeNull = true)
    private boolean isAdmin;

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

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
