package ru.noname.mysnake.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sessions")
public class Session {

    public Session(){

    }

    public Session(String uuid, int userId){
        this.uuid = uuid;
        this.userId = userId;
    }

    @DatabaseField(columnName = "uuid", canBeNull = false)
    private String uuid;

    @DatabaseField(columnName = "user_id", canBeNull = false, unique = true, id = true)
    private int userId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
