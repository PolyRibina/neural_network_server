--
-- Файл сгенерирован с помощью SQLiteStudio v3.3.2 в Вс мар 28 22:05:34 2021
--
-- Использованная кодировка текста: System
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Таблица: chats
DROP TABLE IF EXISTS chats;

CREATE TABLE chats (
    id   INTEGER PRIMARY KEY AUTOINCREMENT
                 UNIQUE
                 NOT NULL,
    name STRING  NOT NULL
);


-- Таблица: links
DROP TABLE IF EXISTS links;

CREATE TABLE links (
    user_id INTEGER NOT NULL,
    chat_id INTEGER NOT NULL,
    UNIQUE (
        user_id,
        chat_id
    )
);


-- Таблица: messages
DROP TABLE IF EXISTS messages;

CREATE TABLE messages (
    id        INTEGER  PRIMARY KEY AUTOINCREMENT
                       UNIQUE
                       NOT NULL,
    user_id   INTEGER  NOT NULL,
    chat_id   INTEGER  NOT NULL,
    create_at DATETIME NOT NULL,
    text      TEXT     NOT NULL
);


-- Таблица: users
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id       INTEGER     PRIMARY KEY AUTOINCREMENT
                         UNIQUE
                         NOT NULL,
    name     STRING      UNIQUE
                         NOT NULL,
    password STRING (32) NOT NULL
);


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
