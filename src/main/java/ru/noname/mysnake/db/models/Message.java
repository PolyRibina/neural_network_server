package ru.noname.mysnake.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "messages")
public class Message {

    public Message(){

    }

    public Message(int userId, int chatId, String text, Date createAt){
        this.chatId = chatId;
        this.userId = userId;
        this.text = text;
        this.createAt = createAt;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "user_id", canBeNull = false)
    private int userId;

    @DatabaseField(columnName = "chat_id", canBeNull = false)
    private int chatId;

    @DatabaseField(columnName = "create_at", canBeNull = false)
    private Date createAt;

    @DatabaseField(canBeNull = false)
    private String text;

    @DatabaseField(columnName = "file", canBeNull = true)
    private String fileInMessage;

    @DatabaseField(columnName = "type", canBeNull = true)
    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileInMessage() {
        return fileInMessage;
    }

    public void setFileInMessage(String fileInMessage) {
        this.fileInMessage = fileInMessage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
