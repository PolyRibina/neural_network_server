package ru.noname.mysnake.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "chats")
public class Chat {

    public Chat(){

    }
    public Chat(String name){
        this.name = name;
    }

    public Chat(String name, String bio){
        this.name = name;
        this.bio = bio;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name; // название чата

    @DatabaseField(canBeNull = true)
    private String bio; // описание чата

    @DatabaseField(columnName = "is_private_dialog", canBeNull = false)
    private Boolean isPrivateDialog;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIsPrivateDialog() {
        return isPrivateDialog;
    }

    public void setIsPrivateDialog(Boolean isPrivateDialog) {
        this.isPrivateDialog = isPrivateDialog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
