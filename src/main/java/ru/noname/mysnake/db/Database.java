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

    /**


    private void readWriteData() throws Exception {
        // create an instance of Account
        String name = "Jim Coakley";
        Account account = new Account(name);

        // persist the account object to the database
        accountDao.create(account);
        int id = account.getId();
        verifyDb(id, account);

        // assign a password
        account.setPassword("_secret");
        // update the database after changing the object
        accountDao.update(account);
        verifyDb(id, account);

        // query for all items in the database
        List<Account> accounts = accountDao.queryForAll();
        assertEquals("Should have found 1 account matching our query", 1, accounts.size());
        verifyAccount(account, accounts.get(0));

        // loop through items in the database
        int accountC = 0;
        for (Account account2 : accountDao) {
            verifyAccount(account, account2);
            accountC++;
        }
        assertEquals("Should have found 1 account in for loop", 1, accountC);

        // construct a query using the QueryBuilder
        QueryBuilder<Account, Integer> statementBuilder = accountDao.queryBuilder();
        // shouldn't find anything: name LIKE 'hello" does not match our account
        statementBuilder.where().like(Account.NAME_FIELD_NAME, "hello");
        accounts = accountDao.query(statementBuilder.prepare());
        assertEquals("Should not have found any accounts matching our query", 0, accounts.size());

        // should find our account: name LIKE 'Jim%' should match our account
        statementBuilder.where().like(Account.NAME_FIELD_NAME, name.substring(0, 3) + "%");
        accounts = accountDao.query(statementBuilder.prepare());
        assertEquals("Should have found 1 account matching our query", 1, accounts.size());
        verifyAccount(account, accounts.get(0));

        // delete the account since we are done with it
        accountDao.delete(account);
        // we shouldn't find it now
        assertNull("account was deleted, shouldn't find any", accountDao.queryForId(id));
    }
     */


}
