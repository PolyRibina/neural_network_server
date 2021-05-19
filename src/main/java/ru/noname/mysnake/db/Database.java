package ru.noname.mysnake.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ru.noname.mysnake.db.models.*;


public class Database {

    private final static String DATABASE_URL = "jdbc:sqlite:sqlite\\my.db";

    private Dao<User, Integer> userDao;
    private Dao<Chat, Integer> chatDao;
    private Dao<Message, Integer> messageDao;
    private Dao<Link, Integer> linkDao;
    private Dao<Session, Integer> sessionDao;
    ConnectionSource connectionSource = null;

    public void connect() throws Exception {
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            setupDatabase(connectionSource);

        } finally {

            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    private void setupDatabase(ConnectionSource connectionSource) throws Exception {
        userDao = DaoManager.createDao(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, User.class);
        linkDao = DaoManager.createDao(connectionSource, Link.class);
        TableUtils.createTableIfNotExists(connectionSource, Link.class);
        messageDao = DaoManager.createDao(connectionSource, Message.class);
        TableUtils.createTableIfNotExists(connectionSource, Message.class);
        chatDao = DaoManager.createDao(connectionSource, Chat.class);
        TableUtils.createTableIfNotExists(connectionSource, Chat.class);
        sessionDao = DaoManager.createDao(connectionSource, Session.class);
        TableUtils.createTableIfNotExists(connectionSource, Session.class);
    }

    public Dao<User, Integer> getUserDao() {
        return userDao;
    }

    public Dao<Chat, Integer> getChatDao() {
        return chatDao;
    }

    public Dao<Message, Integer> getMessageDao() {
        return messageDao;
    }

    public Dao<Link, Integer> getLinkDao() {
        return linkDao;
    }

    public Dao<Session, Integer> getSessionDao() {
        return sessionDao;
    }

    private static Database db;

    public static Database getInstance() {
        if (db == null) {
            db = new Database();
        }
        return db;
    }
}
