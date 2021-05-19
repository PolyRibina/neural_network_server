package ru.noname.mysnake.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "users")
public class User {

    public User(){

    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
    }
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String password;

    @DatabaseField(canBeNull = true)
    private String bio;

    private Boolean isOnline = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }
    public boolean getOnline() {
        return isOnline;
    }
}
